package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

import static org.junit.jupiter.api.Assertions.*;

public class InitMeansTestOutcomeCalculatorTest {


    private Outcome meansTestOutcome;

    private boolean fullAssessmentPossible;
    private InitAssessmentResult initAssessmentResult;

    @Test
    public void FAILInitResultAndAssementNotPossible() throws UndefinedOutcomeException {
        initAssessmentResult = InitAssessmentResult.FAIL;
        fullAssessmentPossible = false;

        Outcome outcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(outcome, Outcome.INELIGIBLE);
    }

    @Test
    public void FAILInitResultAndAssementPossible() throws UndefinedOutcomeException {
        initAssessmentResult = InitAssessmentResult.FAIL;
        fullAssessmentPossible = true;

        Outcome outcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(outcome, null);
    }

    @Test
    public void PASSInitResultAndAssementPossible() throws UndefinedOutcomeException {
        initAssessmentResult = InitAssessmentResult.PASS;
        fullAssessmentPossible = true;

        Outcome outcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(outcome, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void FULLResultAndAssementPossible() throws UndefinedOutcomeException {
        initAssessmentResult = InitAssessmentResult.FULL;
        fullAssessmentPossible = true;

        Outcome outcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);

        assertEquals(outcome,null);

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

        Exception exception = assertThrows(UndefinedOutcomeException.class, () -> {
            InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult,fullAssessmentPossible);
        });

        String expectedMsg = "InitMeansTestOutcome: Undefined outcome";
        assertTrue(exception.getMessage().startsWith(expectedMsg));

    }

}
