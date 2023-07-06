package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Means Test Outcome values for result of assessment
 */
@Getter
public enum MeansTestOutcome {
    ELIGIBLE_WITH_CONTRIBUTION("Eligible with income contribution"),
    ELIGIBLE_WITH_NO_CONTRIBUTION("Eligible with no income contribution"),
    INELIGIBLE("Ineligible");

    private String outcome;

    private MeansTestOutcome(String outcome){
        this.outcome = outcome;
    }
}