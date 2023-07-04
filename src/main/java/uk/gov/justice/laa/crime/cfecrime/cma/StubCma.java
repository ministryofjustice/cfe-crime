package uk.gov.justice.laa.crime.cfecrime.cma;

import uk.gov.justice.laa.crime.cfecrime.cma.request.CmaRequest;
import uk.gov.justice.laa.crime.cfecrime.cma.response.*;
import uk.gov.justice.laa.crime.cfecrime.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.utils.FullMeansTestOutcome;

import java.math.BigDecimal;
import java.util.List;

public class StubCma implements Cma {
    public ValueList callCma(CmaRequest request) {
        DecimalValue lowerThreshold = new DecimalValue("lower_threshold", new BigDecimal("0.0"));
        DecimalValue upperThreshold = new DecimalValue("upper_threshold", new BigDecimal("0.0"));
        DecimalValue aggregatedGrossIncome = new DecimalValue("aggregrated_gross_income", new BigDecimal("0.0"));
        DecimalValue adjustedLivingAllowance = new DecimalValue("adjusted_living_allowance", new BigDecimal("0.0"));
        DecimalValue adjustedIncome = new DecimalValue("adjusted_income", new BigDecimal("0.0"));
        StringValue result = new StringValue("result", AssessmentResult.PASS.name());
        FullMeansTestOutcome fmto = new FullMeansTestOutcome(AssessmentResult.PASS, CaseType.COMMITAL);
        Outcome fullMeansTestOutcome = fmto.getFullMeansOutcome();

        StringValue outcome = new StringValue("outcome", fullMeansTestOutcome.getOutcome());
        BooleanValue fullAssessmentAvailable = new BooleanValue("full_assessment_available", false);

        ValueList initialMeansAssessment = new ValueList("initial_means_assessment",
                List.of(lowerThreshold, upperThreshold, aggregatedGrossIncome, adjustedLivingAllowance,
                adjustedIncome, result, outcome, fullAssessmentAvailable));

        DecimalValue fullThreshold = new DecimalValue("full_threshold", new BigDecimal("0.0"));
        DecimalValue eligibilityThreshold = new DecimalValue("eligibility_threshold", new BigDecimal("0.0"));
        DecimalValue totalAnnualAggregatedExpenditure = new DecimalValue("total_annual_aggregated_expenditure", new BigDecimal("0.0"));
        DecimalValue disposableIncome = new DecimalValue("disposable_income", new BigDecimal("0.0"));

        ValueList fullMeansAssessment = new ValueList("full_means_assessment", List.of(fullThreshold, eligibilityThreshold, adjustedLivingAllowance,
                totalAnnualAggregatedExpenditure, disposableIncome,
                result, outcome));

        return new ValueList("response", List.of(initialMeansAssessment, fullMeansAssessment));
    }
}
