package uk.gov.justice.laa.crime.cfecrime.cma.enums;

/**
 * OutCome values for result of assessment
 */
public enum MeansTestOutcome {
    ELIGIBLE_WITH_CONTRIBUTION("Eligible with income contribution"),
    ELIGIBLE_WITH_NO_CONTRIBUTION("Eligible with no income contribution"),
    INELIGIBLE("Ineligible");

    private String outcome;

    public String getOutcome(){
        return outcome;
    }

    private MeansTestOutcome(String outcome){
        this.outcome = outcome;
    }
}