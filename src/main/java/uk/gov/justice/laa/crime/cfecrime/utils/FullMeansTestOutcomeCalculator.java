package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;

@UtilityClass
@Slf4j
public class FullMeansTestOutcomeCalculator {

    public static Outcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("FullMeansTestOutcome start. Inputs: caseType = {} magCourtOutcome = {} result = {}", caseType, magCourtOutcome, result);

        Outcome outcome = null;

        if (result == null) {
            outcome = null;
        } else if (isCaseBeingHeardInMagistrateCourt(caseType, magCourtOutcome)) {
            switch (result) {
                case FAIL:
                    outcome = Outcome.INELIGIBLE;
                    break;
                case PASS:
                    outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                    break;
                default:
                    outcome = null;
            }
        } else if (isCaseBeingHeardInCrownCourtExcludingAppeals(caseType, magCourtOutcome)) {
            switch (result) {
                case INEL:
                    outcome = Outcome.INELIGIBLE;
                    break;
                case FAIL:
                    outcome = Outcome.ELIGIBLE_WITH_CONTRIBUTION;
                    break;
                case PASS:
                    outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                    break;
            }
        } else if (caseType == CaseType.APPEAL_CC) {
            switch (result) {
                case FAIL:
                    outcome = Outcome.ELIGIBLE_WITH_CONTRIBUTION;
                    break;
                case PASS:
                    outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                    break;
                default:
                    outcome = null;
            }
        }

        if (outcome == null) {
            throw new RuntimeException("FullMeansTestOutcome: Undefined outcome for these inputs: Full Means Test " +
                                       " caseType = " + caseType + " magCourtOutcome = " + magCourtOutcome + " result = " + result);
        }
        log.info("FullMeansTestOutcome end. Outputs: outcome = {}", outcome);
        return outcome;
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

}
