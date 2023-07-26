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

        BigDecimal fullThreshold = new BigDecimal("0.0");
        BigDecimal eligibilityThreshold = new BigDecimal("0.0");
        BigDecimal totalAnnualAggregatedExpenditure = new BigDecimal("0.0");
        BigDecimal disposableIncome = new BigDecimal("0.0");

        CmaFullResult fullResult = new CmaFullResult(FullAssessmentResult.INEL, disposableIncome, adjustedIncome,
                adjustedLivingAllowance,  totalAnnualAggregatedExpenditure, totalAnnualAggregatedExpenditure,
                eligibilityThreshold);
        return fullResult;
    }
}
