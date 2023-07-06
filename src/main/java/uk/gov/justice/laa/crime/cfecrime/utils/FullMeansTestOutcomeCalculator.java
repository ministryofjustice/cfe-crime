package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import java.util.Set;

@Slf4j
public class FullMeansTestOutcomeCalculator {
    //Case types Heard In Magistrates Court
    static private Set<CaseType> caseTypesHeardInMagistratesCourt = Set.<CaseType>of(CaseType.COMMITAL, CaseType.SUMMARY_ONLY, CaseType.EITHER_WAY);

    //Case types Heard In Crown Court
    static private Set<CaseType> caseTypesHeardInCrownCourt = Set.<CaseType>of(CaseType.INDICTABLE, CaseType.CC_ALREADY, CaseType.EITHER_WAY, CaseType.APPEAL_CC);

    private FullMeansTestOutcomeCalculator(){
    }

    public static MeansTestOutcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("Get the outcome of the Full Means Test. Inputs: result = {} caseType = {} magCourtOutcome = {}", result, caseType, magCourtOutcome);


        MeansTestOutcome meansTestOutcome = null;
        if (result != null && caseType != null && magCourtOutcome != null) {

            if (result == FullAssessmentResult.PASS) {
                if ( caseTypesHeardInMagistratesCourt.contains(caseType) ||
                        caseTypesHeardInCrownCourt.contains(caseType)) {
                    //All Eligible with no contribution
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                }else{
                    // throw exception
                    throw new RuntimeException("Means Test Outcome is not possible. Inputs: result = " + result +
                            " caseType = " + caseType + " magCourtOutcome = " + magCourtOutcome);
                }
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
                if ((caseType == CaseType.EITHER_WAY && magCourtOutcome == MagCourtOutcome.COMMITTED_FOR_TRIAL) ||
                        caseTypesHeardInCrownCourt.contains(caseType)){
                    //"Either way" - offence type that could be heard in Magistrates Court
                    // or Crown Court AND magistrate outcome is COMMITTED_FOR_TRIAL
                    //All Ineligible
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
                }else{
                    // throw exception
                    throw new RuntimeException("Means Test Outcome is not possible. Inputs: result = " + result +
                        " caseType = " + caseType + " magCourtOutcome = " + magCourtOutcome);
                }
            }
        }else{
            // throw exception
            throw new RuntimeException("Means Test Outcome is not possible. Inputs: result = " + result +
                                        " caseType = " + caseType + " magCourtOutcome = " + magCourtOutcome);
        }
        log.info("Outcome of the Full Means Test. Outputs: meansTestOutcome = {}", meansTestOutcome);
        return meansTestOutcome;
    }

}



