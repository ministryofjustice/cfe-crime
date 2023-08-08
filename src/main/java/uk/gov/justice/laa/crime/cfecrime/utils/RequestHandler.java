package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.Result.Outcome;
import uk.gov.justice.laa.crime.cfecrime.api.*;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Slf4j
@UtilityClass
public class RequestHandler {

    public static CfeCrimeResponse handleRequest(CfeCrimeRequest request) throws UndefinedOutcomeException {

        Boolean under18 = null;
        Boolean passported = null;
        Outcome outcome = null;
        MeansTestOutcome meansTestOutcome = null;

        if (request.getSectionUnder18() != null) {
            under18 = request.getSectionUnder18().getClientUnder18();
        }
        if (request.getSectionPassportedBenefit() != null) {
            passported = request.getSectionPassportedBenefit().getPassportedBenefit();
        }
        outcome = getOutcomeFromAgeAndPassportedBenefit(under18, passported);

        CfeCrimeResponse response = new CfeCrimeResponse();
        if (outcome != null) {
            Result result = new Result();
            result.setOutcome(outcome);
            response.withResult(result);
        }
        return response;
    }

    private static Outcome getOutcomeFromAgeAndPassportedBenefit(Boolean clientUnder18, Boolean clientPassportedBenefit) {
        Outcome outcome = null;
        if (clientUnder18 != null && clientUnder18.booleanValue()) {
            outcome = Result.Outcome.ELIGIBLE;
        }
        if (clientPassportedBenefit != null && clientPassportedBenefit.booleanValue()) {
            outcome = Result.Outcome.ELIGIBLE;
        }
        return outcome;
    }
}
