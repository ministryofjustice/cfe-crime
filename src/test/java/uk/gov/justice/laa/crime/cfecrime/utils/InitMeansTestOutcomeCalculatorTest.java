package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

import static org.junit.jupiter.api.Assertions.*;

public class InitMeansTestOutcomeCalculatorTest {

    private MeansTestOutcome meansTestOutcome;

    private boolean fullAssessmentPossible;
    private InitAssessmentResult initAssessmentResult;

    @Test
    public void FAILInitResultAndAssementNotPossible() {
        initAssessmentResult = InitAssessmentResult.FAIL;
        fullAssessmentPossible = false;

        MeansTestOutcome oc = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(oc, MeansTestOutcome.INELIGIBLE);
    }

    @Test
    public void FAILInitResultAndAssementPossible() {
        initAssessmentResult = InitAssessmentResult.FAIL;
        fullAssessmentPossible = true;

        MeansTestOutcome oc = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(oc, null);
    }

    @Test
    public void PASSInitResultAndAssementPossible() {
        initAssessmentResult = InitAssessmentResult.PASS;
        fullAssessmentPossible = true;

        MeansTestOutcome oc = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(oc, MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void FULLResultAndAssementPossible() {
        initAssessmentResult = InitAssessmentResult.FULL;
        fullAssessmentPossible = true;

        MeansTestOutcome oc = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(oc,null);

    }

    // unhappy paths
    @Test
    public void nullInitResult() {
        initAssessmentResult = null;
        fullAssessmentPossible = false;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);
        });

        String expectedMsg = "InitMeansTestOutcome: Undefined outcome";
        assertTrue(exception.getMessage().startsWith(expectedMsg));
    }

    @Test
    public void FULLResultAndAssementNotPossible() {
        initAssessmentResult = InitAssessmentResult.FULL;
        fullAssessmentPossible = false;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);
        });

        String expectedMsg = "InitMeansTestOutcome: Undefined outcome";
        assertTrue(exception.getMessage().startsWith(expectedMsg));

    }


}
