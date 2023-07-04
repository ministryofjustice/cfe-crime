package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import uk.gov.justice.laa.crime.cfecrime.cma.response.AssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
@Getter
@Slf4j
public class FullMeansTestOutcome {

    @Getter(AccessLevel.NONE)
    private AssessmentResult result;
    @Getter(AccessLevel.NONE)
    private  CaseType caseType;

    private Outcome fullMeansOutcome;

    public FullMeansTestOutcome(AssessmentResult result, CaseType caseType){
        this.result = result;
        this.caseType = caseType;
        fullMeansOutcome = Outcome.NOT_POSSIBLE;
        if (result != null &&
            caseType != null) {
            fullMeansOutcome = setFullMeansTestOutCome();
        }
    }

    private Outcome setFullMeansTestOutCome() {
        log.debug("Get the outcome of the Full Means Test result = {} caseType = {}", result, caseType);
        Outcome outcome = Outcome.NOT_POSSIBLE;

        if (result == AssessmentResult.INEL) {

            outcome = checkOutcomeINEL();
        }
        if (result == AssessmentResult.PASS) {
            outcome = checkOutcomePass();
        }
        if (result == AssessmentResult.FAIL) {

            outcome = checkOutcomeFail();
        }
        log.info("Outcome of the Full Means Test: {}", outcome);
        return outcome;
    }

    private Outcome checkOutcomeFail() {
        Outcome outcome = Outcome.ELIGIBLE_WITH_CONTRIBUTION;
        switch (caseType) {
            case COMMITAL:
            case SUMMARY_ONLY:
            case EITHER_WAY:
            outcome = Outcome.INELIGIBLE;
        }
        return outcome;
    }

    private Outcome checkOutcomeINEL() {
        Outcome outcome = Outcome.NOT_POSSIBLE;
        if (caseType == CaseType.CC_ALREADY ||
            caseType == CaseType.INDICTABLE ||
            caseType == CaseType.EITHER_WAY) {
            outcome = Outcome.INELIGIBLE;
        }
        return outcome;
    }

    private Outcome checkOutcomePass() {
        Outcome outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        switch (caseType) {
            case COMMITAL:
            case SUMMARY_ONLY:
            case EITHER_WAY:
                outcome = Outcome.ELIGIBLE;
        }
        return outcome;
    }
}



