package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FullMeansTestOutcomeCalculator {

    public static MeansTestOutcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("Get the outcome of the Full Means Test result = {} caseType = {}", result, caseType);

        //Fail data
        Map<Integer,CaseType> failMaps = new HashMap<Integer, CaseType>();
        failMaps.put(1, CaseType.COMMITAL);
        failMaps.put(2, CaseType.SUMMARY_ONLY);
        failMaps.put(3, CaseType.EITHER_WAY);

        MeansTestOutcome meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        if (result != null && caseType != null ) {

            if (result == FullAssessmentResult.PASS) {
                    //Assume all Eligible with no contribution
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
            }
            if (result == FullAssessmentResult.FAIL) {
                meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION;
                // Either way" - offence type that could be heard in Magistrates Court or
                // Crown Court AND magistrate outcome is NOT COMMITTED_FOR_TRIAL
                if (caseType == CaseType.EITHER_WAY && magCourtOutcome != MagCourtOutcome.COMMITTED_FOR_TRIAL) {
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
                } else {
                    if (failMaps.containsValue(caseType)) {
                        meansTestOutcome = MeansTestOutcome.INELIGIBLE;
                    }
                }
            }
            if (result == FullAssessmentResult.INEL){
                    //"Either way" - offence type that could be heard in Magistrates Court
                    // or Crown Court AND magistrate outcome is COMMITTED_FOR_TRIAL
                    //Assume all Ineligible
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
            }
        }else{
            // throw exception
            throw new RuntimeException("Means Test Outcome is not possible");
        }
        log.info("Outcome of the Full Means Test: {}", meansTestOutcome);
        return meansTestOutcome;
    }

}



