package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;

@Slf4j
public class FullMeansTestOutcomeCalculator {

    public static Outcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("FullMeansTestOutcome start. Inputs: caseType = {} magCourtOutcome = {} result = {}", caseType, magCourtOutcome, result);

        Outcome meansTestOutcome = null;

        if (result == null) {
            meansTestOutcome = null;
        } else if (isCaseBeingHeardInMagistrateCourt(caseType, magCourtOutcome)) {
            switch (result) {
                case FAIL:
                    meansTestOutcome = Outcome.INELIGIBLE;
                    break;
                case PASS:
                    meansTestOutcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                    break;
                default:
                    meansTestOutcome = null;
            }
        } else if (isCaseBeingHeardInCrownCourtExcludingAppeals(caseType, magCourtOutcome)) {
            switch (result) {
                case INEL:
                    meansTestOutcome = Outcome.INELIGIBLE;
                    break;
                case FAIL:
                    meansTestOutcome = Outcome.ELIGIBLE_WITH_CONTRIBUTION;
                    break;
                case PASS:
                    meansTestOutcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                    break;
            }
        } else if (caseType == CaseType.APPEAL_CC) {
            switch (result) {
                case FAIL:
                    meansTestOutcome = Outcome.ELIGIBLE_WITH_CONTRIBUTION;
                    break;
                case PASS:
                    meansTestOutcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                    break;
                default:
                    meansTestOutcome = null;
            }
        }

        if (meansTestOutcome == null) {
            throw new RuntimeException("FullMeansTestOutcome: Undefined outcome for these inputs: Full Means Test " +
                                       " caseType = " + caseType + " magCourtOutcome = " + magCourtOutcome + " result = " + result);
        }
        log.info("FullMeansTestOutcome end. Outputs: meansTestOutcome = {}", meansTestOutcome);
        return meansTestOutcome;
    }

    private static boolean isCaseBeingHeardInMagistrateCourt(CaseType caseType, MagCourtOutcome magCourtOutcome) {
        return (
            caseType == CaseType.SUMMARY_ONLY ||
            caseType == CaseType.COMMITAL ||
            (
                caseType == CaseType.EITHER_WAY &&
                magCourtOutcome != MagCourtOutcome.COMMITTED_FOR_TRIAL &&
                magCourtOutcome != null
            )
        );
    }

    private static boolean isCaseBeingHeardInCrownCourtExcludingAppeals(CaseType caseType, MagCourtOutcome magCourtOutcome) {
        return (
            caseType == CaseType.INDICTABLE ||
            caseType == CaseType.CC_ALREADY ||
            (
                caseType == CaseType.EITHER_WAY &&
                magCourtOutcome == MagCourtOutcome.COMMITTED_FOR_TRIAL
            )
        );
    }

    private FullMeansTestOutcomeCalculator(){
    }

}
