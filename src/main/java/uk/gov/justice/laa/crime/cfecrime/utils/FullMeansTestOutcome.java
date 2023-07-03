package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import uk.gov.justice.laa.crime.cfecrime.cma.response.AssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
@Getter
@Slf4j
public class FullMeansTestOutcome {

    private AssessmentResult result;
    private  CaseType caseType;

    private Outcome fullMeansOutcome;

    public FullMeansTestOutcome(AssessmentResult result, CaseType caseType){
        this.result = result;
        this.caseType = caseType;

        fullMeansOutcome = setFullMeansTestOutCome();
    }

    private Outcome setFullMeansTestOutCome() {
        log.debug("Get the outcome of the Full Means Test {} {}", result ,caseType );
        Outcome outcome = Outcome.NOT_POSSIBLE;

        if (result != null) {
            switch (result) {
                case INEL:
                    outcome = checkCrownCourtOutcomeINEL();
                    break;
                case PASS:
                    outcome = checkMagCourtOutcomePass();
                    break;
                case FAIL:
                    outcome = checkMagCourtOutcomeFail();
                    break;
            }
        }

        log.info("Outcome of the Full Means Test: {}", outcome);
        return outcome;
    }

    private Outcome checkMagCourtOutcomeFail() {
        Outcome outcome = Outcome.ELIGIBLE_WITH_CONTRIBUTION;
        switch (caseType) {
            case COMMITAL:
            case SUMMARY_ONLY:
            case EITHER_WAY:
            outcome = Outcome.INELIGIBLE;
        }
        return outcome;
    }

    private Outcome checkCrownCourtOutcomeINEL() {
        Outcome outcome = Outcome.NOT_POSSIBLE;
        if (caseType == CaseType.CC_ALREADY ||
            caseType == CaseType.INDICTABLE ||
            caseType == CaseType.EITHER_WAY) {
            outcome = Outcome.INELIGIBLE;
        }
        return outcome;
    }

    private Outcome checkMagCourtOutcomePass() {
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



