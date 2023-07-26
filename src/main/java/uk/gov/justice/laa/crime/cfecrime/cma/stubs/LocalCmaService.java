package uk.gov.justice.laa.crime.cfecrime.cma.stubs;

import uk.gov.justice.laa.crime.cfecrime.cma.enums.*;
import uk.gov.justice.laa.crime.cfecrime.cma.response.*;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;

import javax.naming.Context;
import java.math.BigDecimal;

public class LocalCmaService implements ICmaService {

    private Context context;

    @Override
    public CmaApiResponse callCma(CmaApiRequest request) {
        /*
        DecimalValue lowerThreshold = new DecimalValue("lower_threshold", new BigDecimal("0.0"));
        DecimalValue upperThreshold = new DecimalValue("upper_threshold", new BigDecimal("0.0"));
        DecimalValue aggregatedGrossIncome = new DecimalValue("aggregrated_gross_income", new BigDecimal("0.0"));
        DecimalValue adjustedLivingAllowance = new DecimalValue("adjusted_living_allowance", new BigDecimal("0.0"));
        DecimalValue adjustedIncome = new DecimalValue("adjusted_income", new BigDecimal("0.0"));
        StringValue result = new StringValue("result", AssessmentResult.PASS.name());
        StringValue outcome = new StringValue("outcome", MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION.name());
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

         */
        CmaFullResult fullResult = getFullResult();
        CmaInitialResult initialResult = getIntialResult();

        return new CmaApiResponse(fullResult,initialResult);
    }

    private CmaInitialResult getIntialResult(){

        BigDecimal lowerThreshold = new BigDecimal(0);
        BigDecimal upperThreshold = new BigDecimal(0);

        CmaInitialResult initResult = new CmaInitialResult(InitAssessmentResult.FULL,lowerThreshold,upperThreshold,false);
        return initResult;
    }

    private CmaFullResult getFullResult(){
        BigDecimal aggregatedGrossIncome =  new BigDecimal("0.0");
        BigDecimal adjustedLivingAllowance = new BigDecimal("0.0");
        BigDecimal adjustedIncome = new BigDecimal("0.0");
        StringValue result = new StringValue("result", AssessmentResult.PASS.name());
        StringValue outcome = new StringValue("outcome", MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION.name());
        BooleanValue fullAssessmentAvailable = new BooleanValue("full_assessment_available", false);
/*
        ValueList initialMeansAssessment = new ValueList("initial_means_assessment",
                List.of(lowerThreshold, upperThreshold, aggregatedGrossIncome, adjustedLivingAllowance,
                        adjustedIncome, result, outcome, fullAssessmentAvailable));
*/
        BigDecimal fullThreshold = new BigDecimal("0.0");
        BigDecimal eligibilityThreshold = new BigDecimal("0.0");
        BigDecimal totalAnnualAggregatedExpenditure = new BigDecimal("0.0");
        BigDecimal disposableIncome = new BigDecimal("0.0");
/*
        ValueList fullMeansAssessment = new ValueList("full_means_assessment", List.of(fullThreshold, eligibilityThreshold, adjustedLivingAllowance,
                totalAnnualAggregatedExpenditure, disposableIncome,
                result, outcome));
*/
        CmaFullResult fullResult = new CmaFullResult(FullAssessmentResult.INEL, disposableIncome, adjustedIncome,
                adjustedLivingAllowance,  totalAnnualAggregatedExpenditure, totalAnnualAggregatedExpenditure,
                eligibilityThreshold);
        return fullResult;
    }
}
