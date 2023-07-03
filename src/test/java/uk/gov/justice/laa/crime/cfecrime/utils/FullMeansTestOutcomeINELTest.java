package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.cma.response.AssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FullMeansTestOutcomeINELTest {

    private Outcome fullMeansTestOutcome;

    private CaseType caseType;
    private AssessmentResult result;

    @Test
    public void givenAssessmentResultINEL_WhenCaseType_CC_ALREADY_ThenIsInEligible() {
        caseType = CaseType.CC_ALREADY;
        result = AssessmentResult.INEL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultINEL_WhenCaseType_INDICTABLE_ThenIsInEligible() {
        caseType = CaseType.INDICTABLE;
        result = AssessmentResult.INEL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultINEL_WhenCaseType_EITHER_WAY_ThenIsInEligible() {
        caseType = CaseType.EITHER_WAY;
        result = AssessmentResult.INEL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultINEL_WhenCaseType_APPEAL_CC_ThenIsNotPossible() {
        caseType = CaseType.APPEAL_CC;
        result = AssessmentResult.INEL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.NOT_POSSIBLE);
    }

    @Test
    public void givenAssessmentResultINEL_WhenCaseType_SUMMARY_ONLY_ThenIsNotPossible() {
        caseType = CaseType.SUMMARY_ONLY;
        result = AssessmentResult.INEL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.NOT_POSSIBLE);
    }

    @Test
    public void givenAssessmentResultINEL_WhenCaseType_COMMITAL_ThenIsNotPossible() {
        caseType = CaseType.COMMITAL;
        result = AssessmentResult.INEL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.NOT_POSSIBLE);
    }
}
