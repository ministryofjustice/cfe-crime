package uk.gov.justice.laa.crime.cfecrime.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils.CmaResponseUtil;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandler;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class InitialMeansTestStepDefs {

    private static Logger log = Logger.getLogger(String.valueOf(InitialMeansTestStepDefs.class));
    private CfeCrimeRequest cfeCrimeRequest = null;
    private CfeCrimeResponse cfeCrimeResponse = null;

    private List<InputData> inputDataList = null;
    private List<OutputData> outputExpectedList = new ArrayList<OutputData>();

    private LocalCmaService  cmaService;
    RequestHandler requestHandler = null;

    @BeforeStep
    public void init() {
        cfeCrimeRequest = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(cfeCrimeRequest, StatelessRequestType.BOTH);
        cmaService = new LocalCmaService(InitAssessmentResult.PASS, FullAssessmentResult.INEL, false);
        requestHandler = new RequestHandler(cmaService);

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
    public OutputData initMeansTestOutputDataDataEntryTransformer(Map<String,String> row){

        OutputData data =  new OutputData();
        if (!row.get("InitialMeansTest").isEmpty()) {
            data.MeansTestOutcome = Outcome.valueOf(row.get("InitialMeansTest"));
        }
        return data;

    }

    @Given("the following input data")
    public void the_following_input_data(List<InputData> inputDataList){
        this.inputDataList = inputDataList;
    }
    @Given("I call CMA")
    public void i_call_cma() {
        for (InputData inputData: inputDataList) {

            OutputData outputData= new OutputData();

            cfeCrimeRequest = new CfeCrimeRequest();
            RequestTestUtil.setAssessment(cfeCrimeRequest, StatelessRequestType.BOTH);
            RequestTestUtil.setSectionInitMeansTest(cfeCrimeRequest, inputData.caseType, inputData.magCourtOutcome);
            RequestTestUtil.setSectionFullMeansTest(cfeCrimeRequest);
            try {
                CmaResponseUtil.setCmaResponse(cmaService,inputData.fullAssessmentPossible, inputData.fullAssessmentResult,  inputData.initAssessmentResult);
                cfeCrimeResponse  = requestHandler.handleRequest(cfeCrimeRequest);
            } catch (UndefinedOutcomeException e) {
                String jsonString = null;
                try {
                    jsonString = RequestTestUtil.getRequestAsJson(cfeCrimeRequest);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                log.info("CfeCrimeRequest = "+ jsonString);
                throw new RuntimeException(e);
            }

            outputData.MeansTestOutcome = cfeCrimeResponse.getOutcome();
            outputExpectedList.add(outputData);
        }

    }
     @Then("I should see the following response from initMeansTest")
    public void i_should_see_the_following_results(List<OutputData> outputDataList) {

        int i = 0;
        for (OutputData outputData: outputDataList) {
            OutputData expectedOutput = outputExpectedList.get(i);

            assertEquals(expectedOutput.MeansTestOutcome, outputData.MeansTestOutcome);
            i++;
        }

    }

}