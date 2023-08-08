package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;

import static org.junit.jupiter.api.Assertions.*;

public class FullMeansTestOutcomeCalculatorTest {

    private Outcome fullMeansTestOutcome;

    private CaseType caseType;
    private FullAssessmentResult result;
    private MagCourtOutcome magCourtOutcome;

    @Test
    public void INELResultAndHeardInCrownCourt() {
        result = FullAssessmentResult.INEL;
        caseType = CaseType.EITHER_WAY;
        magCourtOutcome = MagCourtOutcome.COMMITTED_FOR_TRIAL;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.INELIGIBLE);
    }

    @Test
    public void FAILResultAndHeardInMagistratesCourt() {
        result = FullAssessmentResult.FAIL;
        caseType = CaseType.COMMITAL;
        magCourtOutcome = null;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.INELIGIBLE);
    }

    @Test
    public void FAILResultAndHeardInCrownCourt() {
        result = FullAssessmentResult.FAIL;
        caseType = CaseType.INDICTABLE;
        magCourtOutcome = null;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void FAILResultAndAppealHeardInCrownCourt() {
        result = FullAssessmentResult.FAIL;
        caseType = CaseType.APPEAL_CC;
        magCourtOutcome = null;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndHeardInMagistratesCourt() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.SUMMARY_ONLY;
        magCourtOutcome = null;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndHeardInMagistratesCourtEitherWay() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.EITHER_WAY;
        magCourtOutcome = MagCourtOutcome.RESOLVED_IN_MAGS;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndHeardInCrownCourt() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.CC_ALREADY;
        magCourtOutcome = null;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndAppealHeardInCrownCourt() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.APPEAL_CC;
        magCourtOutcome = null;

        Outcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    // unhappy paths

    @Test
    public void nullCaseType() {
        result = FullAssessmentResult.PASS;
        caseType = null;
        magCourtOutcome = null;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);
        });

        String expectedMsg = "FullMeansTestOutcome: Undefined outcome";
        assertTrue(exception.getMessage().startsWith(expectedMsg));
    }

    @Test
    public void nullResult() {
        result = null;
        caseType = CaseType.EITHER_WAY;
        magCourtOutcome = MagCourtOutcome.RESOLVED_IN_MAGS;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);
        });

        String expectedMsg = "FullMeansTestOutcome: Undefined outcome";
        assertTrue(exception.getMessage().startsWith(expectedMsg));
    }

    @Test
    public void INELResultAndHeardInMagistratesCourt() {
        result = FullAssessmentResult.INEL;
        caseType = CaseType.SUMMARY_ONLY;
        magCourtOutcome = null;

        // INEL should only happen for crown court cases, not this mags court hearing
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);
        });

        String expectedMsg = "FullMeansTestOutcome: Undefined outcome";
        assertTrue(exception.getMessage().startsWith(expectedMsg));
    }

    @Test
    public void INELResultAndAppealHeardInCrownCourt() {
        result = FullAssessmentResult.INEL;
        caseType = CaseType.APPEAL_CC;
        magCourtOutcome = null;

        // INEL should only happen for non-appeal crown court cases, not this appeal to the crown court hearing
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);
        });

        String expectedMsg = "FullMeansTestOutcome: Undefined outcome";
        assertTrue(exception.getMessage().startsWith(expectedMsg));
    }
}
