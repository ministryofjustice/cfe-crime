package uk.gov.justice.laa.crime.cfecrime.steps;

import io.cucumber.java.BeforeStep;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.*;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.FullMeansTestOutcomeCalculator;
import uk.gov.justice.laa.crime.cfecrime.utils.InitMeansTestOutcomeCalculator;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.StatelessFullResult;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.StatelessInitialResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InitialMeansTestStepDefs {

    private CfeCrimeRequest request = null;
    private CfeCrimeResponse response = null;

    private StatelessApiRequest cmarequest = null;

    public List<InputData> inputDataList = null;

    public List<OutputData> outputDataList = null;
    public List<OutputData> outputExpectedList = new ArrayList<>();

    public StatelessApiResponse cmaResponse = null;
    StatelessApiRequest cmaRequest = new StatelessApiRequest();

    @BeforeStep
    public void init() {
        request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request);

    }

    @DataTableType(replaceWithEmptyString = "[Blank]")
    public InputData inputDataentryTransformer(Map<String,String> row){

        InputData data =  new InputData();
        data.initAssessmentResult= InitAssessmentResult.valueOf(row.get("InitAssessmentResult"));
        data.fullAssessmentResult = null;
        if (!row.get("FullAssessmentResult").isEmpty()) {
            data.fullAssessmentResult = FullAssessmentResult.valueOf(row.get("FullAssessmentResult"));
        }
        data.magCourtOutcome = null;
        if (!row.get("MagistrateCourt").isEmpty()) {
            data.magCourtOutcome = MagCourtOutcome.valueOf(row.get("MagistrateCourt"));
        }
        data.caseType = CaseType.valueOf(row.get("CaseType"));
        data.fullAssessmentPossible= Boolean.parseBoolean(row.get("FullAssessmentPossible"));
        return data;

    }

    @DataTableType(replaceWithEmptyString = "[Blank]")
    public OutputData outputDataentryTransformer(Map<String,String> row){

        OutputData data =  new OutputData();
        data.fullMeansTest = null;
        data.initialMeansTest = null;
        if (!row.get("FullMeansTest").isEmpty()) {
            data.fullMeansTest = Outcome.valueOf(row.get("FullMeansTest"));
        }
        if (!row.get("InitialMeansTest").isEmpty()) {
            data.initialMeansTest = Outcome.valueOf(row.get("InitialMeansTest"));
        }
        return data;

    }

    @Given("the following input data and I call CMA")
    public void the_following_input_data_i_call_cma(List<InputData> inputDataList){

        this.inputDataList = inputDataList;

        for (InputData inputData: inputDataList) {
            LocalCmaService cmaService = new LocalCmaService();
            cmaService.setFullAssessmentPossible(inputData.fullAssessmentPossible);
            cmaService.setFullAssessmentResult(inputData.fullAssessmentResult);
            cmaService.setInitAssessmentResult(inputData.initAssessmentResult);
            cmaResponse = cmaService.callCma(cmaRequest);

            StatelessInitialResult statelessInitialResult = cmaResponse.getInitialMeansAssessment();
            InitAssessmentResult initAssessmentResult = statelessInitialResult.getResult();
            StatelessFullResult statelessFullResult = cmaResponse.getFullMeansAssessment();
            FullAssessmentResult fullAssessmentResult = statelessFullResult.getResult();
            boolean fullAssessmentPossible = statelessInitialResult.isFullAssessmentPossible();
            //assertEquals(statelessFullResult.getResult(), result);
            Outcome initOutcome = null;
            Outcome fullOutcome = null;

            try {
                initOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult, fullAssessmentPossible);
                fullOutcome = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(fullAssessmentResult, inputData.caseType, inputData.magCourtOutcome);

            } catch (UndefinedOutcomeException e) {
                throw new RuntimeException(e);
            }

            OutputData outputData= new OutputData();
            outputData.initialMeansTest = initOutcome;
            outputData.fullMeansTest = fullOutcome;
            outputExpectedList.add(outputData);
        }


    }

    @Then("I should see the following results of the calculation")
    public void i_should_see_the_following_results(List<OutputData> outputDataList) {
        this.outputDataList = outputDataList;
        int i = 0;
        for (OutputData outputData: outputDataList) {
            OutputData expectedOutput = outputExpectedList.get(i);

            assertEquals(expectedOutput.initialMeansTest, outputData.initialMeansTest);
            assertEquals(expectedOutput.fullMeansTest, outputData.fullMeansTest);
            i++;
        }

    }

}