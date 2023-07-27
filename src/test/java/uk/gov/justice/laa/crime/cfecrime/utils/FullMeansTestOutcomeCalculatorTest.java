package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;

import static org.junit.jupiter.api.Assertions.*;

public class FullMeansTestOutcomeCalculatorTest {

    private MeansTestOutcome fullMeansTestOutcome;

    private CaseType caseType;
    private FullAssessmentResult result;
    private MagCourtOutcome magCourtOutcome;

    @Test
    public void INELResultAndHeardInCrownCourt() {
        result = FullAssessmentResult.INEL;
        caseType = CaseType.EITHER_WAY;
        magCourtOutcome = MagCourtOutcome.COMMITTED_FOR_TRIAL;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }

    @Test
    public void FAILResultAndHeardInMagistratesCourt() {
        result = FullAssessmentResult.FAIL;
        caseType = CaseType.COMMITAL;
        magCourtOutcome = null;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }

    @Test
    public void FAILResultAndHeardInCrownCourt() {
        result = FullAssessmentResult.FAIL;
        caseType = CaseType.INDICTABLE;
        magCourtOutcome = null;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void FAILResultAndAppealHeardInCrownCourt() {
        result = FullAssessmentResult.FAIL;
        caseType = CaseType.APPEAL_CC;
        magCourtOutcome = null;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndHeardInMagistratesCourt() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.SUMMARY_ONLY;
        magCourtOutcome = null;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndHeardInMagistratesCourtEitherWay() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.EITHER_WAY;
        magCourtOutcome = MagCourtOutcome.RESOLVED_IN_MAGS;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndHeardInCrownCourt() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.CC_ALREADY;
        magCourtOutcome = null;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void PASSResultAndAppealHeardInCrownCourt() {
        result = FullAssessmentResult.PASS;
        caseType = CaseType.APPEAL_CC;
        magCourtOutcome = null;

        MeansTestOutcome oc = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
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
