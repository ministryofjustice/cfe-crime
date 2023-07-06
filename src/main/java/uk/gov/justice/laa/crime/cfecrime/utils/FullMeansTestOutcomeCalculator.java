package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import java.util.Set;

@Slf4j
public class FullMeansTestOutcomeCalculator {

    private FullMeansTestOutcomeCalculator(){
        //FullMeansTestOutcomeCalculator class
        //to achieve code coverage 100%
    }

    public static MeansTestOutcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("Get the outcome of the Full Means Test result = {} caseType = {} magCourtOutcome = {}", result, caseType, magCourtOutcome);

        //Fail result: Heard In Magistrates Court
        Set<CaseType> caseTypesHeardInMagistratesCourt = Set.<CaseType>of(CaseType.COMMITAL, CaseType.SUMMARY_ONLY, CaseType.EITHER_WAY);

        MeansTestOutcome meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        if (result != null && caseType != null ) {

            if (result == FullAssessmentResult.PASS) {
                    //All Eligible with no contribution
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
            }
            if (result == FullAssessmentResult.FAIL) {
                // Either way" - offence type that could be heard in Magistrates Court or
                // Crown Court AND magistrate outcome is NOT COMMITTED_FOR_TRIAL
                if ((caseType == CaseType.EITHER_WAY && magCourtOutcome != MagCourtOutcome.COMMITTED_FOR_TRIAL) ||
                        caseTypesHeardInMagistratesCourt.contains(caseType)) {
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
                }else{
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION;
                }
            }
            if (result == FullAssessmentResult.INEL){
                    //"Either way" - offence type that could be heard in Magistrates Court
                    // or Crown Court AND magistrate outcome is COMMITTED_FOR_TRIAL
                    //All Ineligible
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
            }
        }else{
            // throw exception
            throw new RuntimeException("Means Test Outcome is not possible.");
        }
        log.info("Outcome of the Full Means Test: {}", meansTestOutcome);
        return meansTestOutcome;
    }

}



