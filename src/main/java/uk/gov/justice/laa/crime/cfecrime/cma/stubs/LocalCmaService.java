package uk.gov.justice.laa.crime.cfecrime.cma.stubs;

import uk.gov.justice.laa.crime.cfecrime.api.stateless.*;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.*;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

import javax.naming.Context;
import java.math.BigDecimal;

public class LocalCmaService implements ICmaService {

    private Context context;

    @Override
    public StatelessApiResponse callCma(StatelessApiRequest request) {

        StatelessFullResult fullResult = getFullResult();
        StatelessInitialResult initialResult = getIntialResult();

        StatelessApiResponse response = new StatelessApiResponse()
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
