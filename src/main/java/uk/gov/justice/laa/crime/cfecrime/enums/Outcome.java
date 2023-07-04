package uk.gov.justice.laa.crime.cfecrime.enums;

/**
 * OutCome values for result of assessment
 */
public enum Outcome {
    ELIGIBLE("Eligible"),
    ELIGIBLE_WITH_CONTRIBUTION("Eligible with income contribution"),
    ELIGIBLE_WITH_NO_CONTRIBUTION("Eligible with no income contribution"),
    INELIGIBLE("Ineligible"),
    NOT_POSSIBLE("not possible");

    private String outcome;

    public String getOutcome(){
        return outcome;
    }

    private Outcome(String outcome){
        this.outcome = outcome;
    }
}