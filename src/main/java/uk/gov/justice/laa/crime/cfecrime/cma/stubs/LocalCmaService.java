package uk.gov.justice.laa.crime.cfecrime.cma.stubs;

import uk.gov.justice.laa.crime.cfecrime.api.cma.CmaApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.cma.CmaApiResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.*;
import uk.gov.justice.laa.crime.cfecrime.cma.response.*;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;

import javax.naming.Context;
import java.math.BigDecimal;

public class LocalCmaService implements ICmaService {

    private Context context;

    @Override
    public CmaApiResponse callCma(CmaApiRequest request) {

        StatelessFullResult fullResult = getFullResult();
        StatelessInitialResult initialResult = getIntialResult();

        CmaApiResponse response = new CmaApiResponse()
                                        .withFullMeansAssessment(fullResult)
                                        .withInitialMeansAssessment(initialResult);
        return response;
    }

    private  StatelessInitialResult getIntialResult(){

        BigDecimal lowerThreshold = new BigDecimal(0);
        BigDecimal upperThreshold = new BigDecimal(0);

        StatelessInitialResult initResult = new StatelessInitialResult(InitAssessmentResult.FULL, lowerThreshold,
                upperThreshold,false);
        return initResult;
    }

    private StatelessFullResult getFullResult(){
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

        StatelessFullResult  fullResult = new StatelessFullResult(FullAssessmentResult.INEL, disposableIncome, adjustedIncome,
                adjustedLivingAllowance,  totalAnnualAggregatedExpenditure, totalAnnualAggregatedExpenditure,
                eligibilityThreshold);
        return fullResult;
    }
}
