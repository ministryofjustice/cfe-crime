package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.api.Result.Outcome;
import uk.gov.justice.laa.crime.cfecrime.api.*;

@Slf4j
@UtilityClass
public class RequestHandler {

    public static CfeCrimeResponse handleRequest(CfeCrimeRequest request){

        Boolean under18 = null;
        Boolean passported = null;
        Outcome oc = null;

        if (request.getSectionUnder18() != null) {
            under18 = request.getSectionUnder18().getClientUnder18();
        }
        if (request.getSectionPassportedBenefit() != null) {
            passported = request.getSectionPassportedBenefit().getPassportedBenefit();
        }
        oc = getOutcomeFromAgeAndPassportedBenefit(under18,passported);

        CfeCrimeResponse response = new CfeCrimeResponse();
        if (oc != null) {
            Result result = new Result();
            result.setOutcome(oc);
            response.withResult(result);
        }
        return response;
    }

    private static Outcome getOutcomeFromAgeAndPassportedBenefit(Boolean clientUnder18, Boolean clientPassportedBenefit){
        Outcome outcome = null;
        if (clientUnder18 != null && clientUnder18.booleanValue()){
            outcome = Result.Outcome.ELIGIBLE;
        }
        if (clientPassportedBenefit !=null && clientPassportedBenefit.booleanValue()){
            outcome = Result.Outcome.ELIGIBLE;
        }
        return outcome;
    }
}
