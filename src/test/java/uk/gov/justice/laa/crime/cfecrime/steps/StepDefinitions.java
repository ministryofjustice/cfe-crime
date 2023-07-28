package uk.gov.justice.laa.crime.cfecrime.steps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import uk.gov.justice.laa.crime.cfecrime.api.Under18;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandlerUtil;

public class StepDefinitions {

    @ParameterType(value = "true|True|TRUE|false|False|FALSE")
    public Boolean bool(String value){
        return Boolean.valueOf(value);
    }

    private Under18.Outcome oc = null;

    @Given("Age is Under Eighteen is {string} Passport benefit is {string}")
    public void age_is_under_eighteen_is_passport_benefit_is(String under18, String passportedBenefit) {

         oc = RequestHandlerUtil.getOutcomeFromAgeAndPassport(Boolean.valueOf(under18),Boolean.valueOf(passportedBenefit));
    }
    @Then("the response will return {string}")
    public void the_response_will_return(String string) {
        if (oc != null){
            string = oc.name();
        }else{
            string = null;
        }
    }
}