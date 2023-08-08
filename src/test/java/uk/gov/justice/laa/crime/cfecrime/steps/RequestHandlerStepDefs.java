package uk.gov.justice.laa.crime.cfecrime.steps;

import io.cucumber.java.BeforeStep;
import io.cucumber.java.ParameterType;
import io. cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.Result;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandler;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestHandlerStepDefs {

    private CfeCrimeRequest request = null;
    @BeforeStep
    public void init(){
        request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request);

    }

    @ParameterType(value = "true|True|TRUE|false|False|FALSE")
    public Boolean bool(String value){
        return Boolean.valueOf(value);
    }

    private Result.Outcome oc = null;
    @Given("Client Under Eighteen {string} Passport benefited {string}")
    public void client_under_eighteen_passport_benefited(String under18, String passportedBenefit) throws UndefinedOutcomeException {
       if (Boolean.valueOf(under18)) {
           RequestTestUtil.setSectionUnder18(request, true);
       }

        if (Boolean.valueOf(passportedBenefit)) {
            RequestTestUtil.setSectionPassportBenefit(request, true);
        }

        CfeCrimeResponse response = RequestHandler.handleRequest(request);
        assertEquals(response.getResult().getOutcome(), Result.Outcome.ELIGIBLE);
    }

    @Then("the response will return {string}")
    public void the_response_will_return(String string) {
        if (oc != null){
            string = oc.name();
        }else{
            string = null;
        }
    }

    @Given("Client not Under Eighteen {string} not Passport benefited {string}")
    public void client_not_under_eighteen_not_passport_benefited(String under18, String passportedBenefit) throws UndefinedOutcomeException {
        if (Boolean.valueOf(under18)) {
            RequestTestUtil.setSectionUnder18(request, true);
        }

        if (Boolean.valueOf(passportedBenefit)) {
            RequestTestUtil.setSectionPassportBenefit(request, true);
        }

        CfeCrimeResponse response = RequestHandler.handleRequest(request);
        CfeCrimeResponse reaponseExpected = new CfeCrimeResponse();
        assertEquals(response.getResult(), reaponseExpected.getResult());
    }

}