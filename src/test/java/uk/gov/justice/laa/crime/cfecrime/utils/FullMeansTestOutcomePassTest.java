package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.cma.response.AssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullMeansTestOutcomePassTest {

    private Outcome fullMeansTestOutcome;

    private CaseType caseType;
    private AssessmentResult result;

    @Test
    public void givenAssessmentResultPass_WhenCaseType_Null_ThenIsNotPossible() {
        caseType = null;
        result = AssessmentResult.PASS;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.NOT_POSSIBLE);
    }

    @Test
    public void givenAssessmentResultNull_WhenCaseType_EITHER_WAY_ThenIsNotPossible() {
        caseType = CaseType.EITHER_WAY;
        result = null;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.NOT_POSSIBLE);
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_EITHER_WAY_ThenIsEligible() {
        caseType = CaseType.EITHER_WAY;
        result = AssessmentResult.PASS;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE);
    }
    @Test
    public void givenAssessmentResultPass_WhenCaseType_SUMMARY_ONLY_ThenIsEligible() {
        caseType = CaseType.SUMMARY_ONLY;
        result = AssessmentResult.PASS;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE);
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_COMMITAL_ThenIsEligible() {
        caseType = CaseType.COMMITAL;
        result = AssessmentResult.PASS;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE);
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_CC_ALREADY_ThenIsEligibleWithNoContribution() {
        caseType = CaseType.CC_ALREADY;
        result = AssessmentResult.PASS;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_INDICTABLE_ThenIsEligibleWithNoContribution() {
        caseType = CaseType.INDICTABLE;
        result = AssessmentResult.PASS;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void givenAssessmentResultPass_WhenCaseType_APPEAL_CC_ThenIsEligibleWithNoContribution() {
        caseType = CaseType.APPEAL_CC;
        result = AssessmentResult.PASS;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

}
