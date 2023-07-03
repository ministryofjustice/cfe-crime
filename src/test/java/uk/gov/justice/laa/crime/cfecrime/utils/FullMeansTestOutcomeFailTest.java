package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.cma.response.AssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullMeansTestOutcomeFailTest {

    private Outcome fullMeansTestOutcome;

    private CaseType caseType;
    private AssessmentResult result;

    @Test
    public void givenAssessmentResultFail_WhenCaseType_EITHER_WAY_ThenIsInEligible() {
        caseType = CaseType.EITHER_WAY;
        result = AssessmentResult.FAIL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.INELIGIBLE);
    }
    @Test
    public void givenAssessmentResultFail_WhenCaseType_SUMMARY_ONLY_ThenIsInEligible() {
        caseType = CaseType.SUMMARY_ONLY;
        result = AssessmentResult.FAIL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_COMMITAL_ThenIsInEligible() {
        caseType = CaseType.COMMITAL;
        result = AssessmentResult.FAIL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.INELIGIBLE);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_CC_ALREADY_ThenIsEligibleWithContribution() {
        caseType = CaseType.CC_ALREADY;
        result = AssessmentResult.FAIL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_INDICTABLE_ThenIsEligibleWithContribution() {
        caseType = CaseType.INDICTABLE;
        result = AssessmentResult.FAIL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

    @Test
    public void givenAssessmentResultFail_WhenCaseType_APPEAL_CC_ThenIsEligibleWithContribution() {
        caseType = CaseType.APPEAL_CC;
        result = AssessmentResult.FAIL;
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(result, caseType);
        Outcome oc = fmto.getFullMeansOutcome();
        assertEquals(oc, Outcome.ELIGIBLE_WITH_CONTRIBUTION);
    }

}
