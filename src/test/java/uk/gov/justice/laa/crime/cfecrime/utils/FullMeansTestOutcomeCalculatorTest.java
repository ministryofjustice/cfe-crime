package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;

import static org.junit.jupiter.api.Assertions.*;

public class FullMeansTestOutcomeCalculatorTest {

    private MeansTestOutcome fullMeansTestOutcome;

    private CaseType caseType;
    private FullAssessmentResult result;

    @Test
    public void givenAssessmentResultPass_WhenCaseType_UNKNOWN_MagCourt_COMMITTED_FOR_TRIAL_ThenIsInEligible() {
        caseType = CaseType.UNKNOWN;
        result = FullAssessmentResult.PASS;
        Exception exception = assertThrows(RuntimeException.class,() -> {
            MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED_FOR_TRIAL);
        });
        String expectedMsg = "Means Test Outcome is not possible";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_Null_ThenIsNotPossible() {
        caseType = null;
        result = FullAssessmentResult.PASS;
        Exception exception = assertThrows(RuntimeException.class,() -> {
            MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED_FOR_TRIAL);
        });
        String expectedMsg = "Means Test Outcome is not possible";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    public void givenAssessmentResult_Null_WhenCaseType_EITHER_WAY_ThenIsNotPossible() {
        caseType = CaseType.EITHER_WAY;
        result = null;
        Exception exception = assertThrows(RuntimeException.class,() -> {
            MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED);
        });
        String expectedMsg = "Means Test Outcome is not possible";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    public void givenAssessmentResult_WhenCaseType_EITHER_WAY_MagCourtOutcome_NULL_ThenIsNotPossible() {
        caseType = CaseType.EITHER_WAY;
        result = FullAssessmentResult.PASS;
        Exception exception = assertThrows(RuntimeException.class,() -> {
            MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType,null);
        });
        String expectedMsg = "Means Test Outcome is not possible";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_EITHER_WAY_ThenIsInEligible() {
        caseType = CaseType.EITHER_WAY;
        result = FullAssessmentResult.FAIL;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED);
        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_EITHER_WAY_COMMITTED_FOR_TRIAL_ThenIsInEligible() {
        caseType = CaseType.EITHER_WAY;
        result = FullAssessmentResult.FAIL;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED_FOR_TRIAL);
        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_SUMMARY_ONLY_COMMITTED_FOR_TRIAL_ThenIsInEligible() {
        caseType = CaseType.SUMMARY_ONLY;
        result = FullAssessmentResult.FAIL;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED_FOR_TRIAL);
        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_EITHER_WAY_COMMITTED_ThenIsInEligible() {
        caseType = CaseType.EITHER_WAY;
        result = FullAssessmentResult.FAIL;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED);
        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_CC_ALREADY_ThenIsEligibleWithContribution() {
        caseType = CaseType.CC_ALREADY;
        result = FullAssessmentResult.FAIL;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType,MagCourtOutcome.COMMITTED);
        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_EITHER_WAY_ThenIsEligibleWithNoContribution() {
        caseType = CaseType.EITHER_WAY;
        result = FullAssessmentResult.PASS;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType,MagCourtOutcome.COMMITTED);
        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_APPEAL_CC_ThenIsEligibleWithNoContribution() {
        caseType = CaseType.APPEAL_CC;
        result = FullAssessmentResult.PASS;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType,MagCourtOutcome.COMMITTED);
        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }
    @Test
    public void givenAssessmentResultPass_WhenCaseType_INDICTABLE_MagCourt_COMMITTED_FOR_TRIAL_ThenIsInEligible() {
        caseType = CaseType.INDICTABLE;
        result = FullAssessmentResult.PASS;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED_FOR_TRIAL);
        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }
    @Test
    public void givenAssessmentResultINEL_WhenCaseType_EITHER_WAY_MagCourt_COMMITTED_FOR_TRIAL_ThenIsInEligible() {
        caseType = CaseType.EITHER_WAY;
        result = FullAssessmentResult.INEL;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType,MagCourtOutcome.COMMITTED_FOR_TRIAL);
        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }
    @Test
    public void givenAssessmentResultINEL_WhenCaseType_EITHER_WAY_MagCourt_COMMITTED_ThenIsInEligible() {
        caseType = CaseType.EITHER_WAY;
        result = FullAssessmentResult.INEL;
        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED);
        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }
    @Test
    public void givenAssessmentResultINEL_WhenCaseType_COMMITAL__MagCourt_COMMITTED_FOR_TRIAL_ThenIsInEligible() {
        caseType = CaseType.COMMITAL;
        result = FullAssessmentResult.INEL;
        Exception exception = assertThrows(RuntimeException.class,() -> {
            MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, MagCourtOutcome.COMMITTED_FOR_TRIAL);
        });
        String expectedMsg = "Means Test Outcome is not possible";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }
}
