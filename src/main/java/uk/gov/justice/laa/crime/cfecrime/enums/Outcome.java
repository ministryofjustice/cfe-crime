package uk.gov.justice.laa.crime.cfecrime.enums;

import lombok.Getter;

/**
 * Means Test Outcome values for result of assessment
 */
@Getter
public enum Outcome {
    ELIGIBLE_WITH_CONTRIBUTION("Eligible with income contribution"),
    ELIGIBLE_WITH_NO_CONTRIBUTION("Eligible with no income contribution"),
    INELIGIBLE("Ineligible");

    private String outcome;

    private Outcome(String outcome){
        this.outcome = outcome;
    }
}