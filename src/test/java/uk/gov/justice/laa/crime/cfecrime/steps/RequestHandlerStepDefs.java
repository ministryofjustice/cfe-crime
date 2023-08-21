package uk.gov.justice.laa.crime.cfecrime.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandler;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestHandlerStepDefs {
    private static Logger log = Logger.getLogger(String.valueOf(RequestHandlerStepDefs.class));
    private CfeCrimeRequest request = null;

    private LocalCmaService cmaService;
    private RequestHandler requestHandler = null;

    @BeforeStep
    public void init(){
        request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH);
        cmaService = new LocalCmaService(InitAssessmentResult.FULL, FullAssessmentResult.INEL, true);
        requestHandler = new RequestHandler(cmaService);
    }

    @ParameterType(value = "true|True|TRUE|false|False|FALSE")
    public Boolean bool(String value){
        return Boolean.valueOf(value);
    }

    private Outcome outcome = null;
    @Given("Client Under Eighteen {string} Passport benefited {string}")
    public void client_under_eighteen_passport_benefited(String under18, String passportedBenefit) throws UndefinedOutcomeException, JsonProcessingException {
       if (Boolean.valueOf(under18)) {
           RequestTestUtil.setSectionUnder18(request, true);
       }

        if (Boolean.valueOf(passportedBenefit)) {
            RequestTestUtil.setSectionPassportBenefit(request, true);
        }
        String jsonString = RequestTestUtil.getRequestAsJson(request);
        log.info("request = "+ jsonString);
        CfeCrimeResponse response = requestHandler.handleRequest(request);
        assertEquals(response.getOutcome(), Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Then("the response will return {string}")
    public void the_response_will_return(String string) {
        if (outcome != null){
            string = outcome.name();
        }else{
            string = null;
        }
    }

    @Given("Client not Under Eighteen {string} not Passport benefited {string}")
    public void client_not_under_eighteen_not_passport_benefited(String under18, String passportedBenefit) throws UndefinedOutcomeException, JsonProcessingException {
        if (Boolean.valueOf(under18)) {
            RequestTestUtil.setSectionUnder18(request, true);
        }else{
            RequestTestUtil.setSectionUnder18(request, false);
        }

        if (Boolean.valueOf(passportedBenefit)) {
            RequestTestUtil.setSectionPassportBenefit(request, true);
        }else{
            RequestTestUtil.setSectionPassportBenefit(request, false);
        }
        RequestTestUtil.setSectionInitMeansTest(request,CaseType.EITHER_WAY, MagCourtOutcome.COMMITTED_FOR_TRIAL);
        String jsonString = RequestTestUtil.getRequestAsJson(request);
        log.info("request = "+ jsonString);
        CfeCrimeResponse response = requestHandler.handleRequest(request);

        assertEquals(response.getOutcome(),Outcome.INELIGIBLE);
    }

}