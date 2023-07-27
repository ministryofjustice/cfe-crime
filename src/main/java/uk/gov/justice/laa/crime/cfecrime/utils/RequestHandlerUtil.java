package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.api.*;

@Slf4j
public class RequestHandlerUtil {

    /*
     * get the outcome from
     * @params clientUnder18, clientPassportedBenefit
     * @return outcome (if outcome is not determined returns null (for more information)
     *
     */
    public static Under18.Outcome getOutcomeFromAgeAndPassport(boolean clientUnder18, boolean clientPassportedBenefit){
        Under18.Outcome outcome = null;
        if (clientUnder18){
            outcome = Under18.Outcome.ELIGIBLE;
        }
        if (clientPassportedBenefit){
            outcome = Under18.Outcome.ELIGIBLE;
        }
        return outcome;
    }
    private RequestHandlerUtil(){
    }
}
