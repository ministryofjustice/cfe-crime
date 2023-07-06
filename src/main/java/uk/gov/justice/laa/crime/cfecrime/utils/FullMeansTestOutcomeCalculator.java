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
    }

    public static MeansTestOutcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("FullMeansTestOutcome start. Inputs: result = {} caseType = {} magCourtOutcome = {}", result, caseType, magCourtOutcome);

        MeansTestOutcome meansTestOutcome = null;

        // Magistrates' court
        if (caseType == CaseType.SUMMARY_ONLY ||
            caseType == CaseType.COMMITAL ||
            (caseType == CaseType.EITHER_WAY && magCourtOutcome != MagCourtOutcome.COMMITTED_FOR_TRIAL && magCourtOutcome != null)) {
                if (result == FullAssessmentResult.FAIL) {
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
                }
                if (result == FullAssessmentResult.PASS) {
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                }
        }

        // Crown Court
        if (caseType == CaseType.INDICTABLE ||
            caseType == CaseType.CC_ALREADY ||
            (caseType == CaseType.EITHER_WAY && magCourtOutcome == MagCourtOutcome.COMMITTED_FOR_TRIAL)) {
                if (result == FullAssessmentResult.INEL) {
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
                }
                if (result == FullAssessmentResult.FAIL) {
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION;
                }
                if (result == FullAssessmentResult.PASS) {
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                }
        }

        // Appeal to Crown Court
        if (caseType == CaseType.APPEAL_CC) {
                if (result == FullAssessmentResult.FAIL) {
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION;
                }
                if (result == FullAssessmentResult.PASS) {
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                }
        }

        if (meansTestOutcome == null) {
            throw new RuntimeException("FullMeansTestOutcome: Undefined outcome for these inputs: Full Means Test result = " + result +
                                       " caseType = " + caseType + " magCourtOutcome = " + magCourtOutcome);
        }
        log.info("FullMeansTestOutcome end. Outputs: meansTestOutcome = {}", meansTestOutcome);
        return meansTestOutcome;
    }

}



