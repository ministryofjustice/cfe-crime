package uk.gov.justice.laa.crime.cfecrime.cma.stubs;

import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.StatelessFullResult;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.StatelessInitialResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

import java.math.BigDecimal;

public class LocalCmaService implements ICmaService {

    private  InitAssessmentResult initAssessmentResult;
    private  FullAssessmentResult fullAssessmentResult;
    private  boolean fullAssessmentPossible;

    public LocalCmaService(InitAssessmentResult initAssessmentResult,FullAssessmentResult fullAssessmentResult,boolean fullAssessmentPossible){
         this.initAssessmentResult = initAssessmentResult;
         this.fullAssessmentResult = fullAssessmentResult;
         this.fullAssessmentPossible = fullAssessmentPossible;
    }
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
        BigDecimal adjustedIncome = new BigDecimal("0.0");
        BigDecimal lowerThreshold = new BigDecimal(0);
        BigDecimal upperThreshold = new BigDecimal(0);
        BigDecimal totalAggregatedIncome = new BigDecimal(0);

        StatelessInitialResult initResult = new StatelessInitialResult(initAssessmentResult, lowerThreshold,
                upperThreshold,fullAssessmentPossible, adjustedIncome, totalAggregatedIncome);
        return initResult;
    }

    private StatelessFullResult getFullResult(){
        BigDecimal adjustedLivingAllowance = new BigDecimal("0.0");
        BigDecimal eligibilityThreshold = new BigDecimal("0.0");
        BigDecimal totalAnnualAggregatedExpenditure = new BigDecimal("0.0");
        BigDecimal disposableIncome = new BigDecimal("0.0");

        StatelessFullResult  fullResult = new StatelessFullResult(fullAssessmentResult, disposableIncome,
                adjustedLivingAllowance,  totalAnnualAggregatedExpenditure,
                eligibilityThreshold);
        return fullResult;
    }

    public void setFullAssessmentPossible(boolean fullAssessmentPossible) {
        this.fullAssessmentPossible = fullAssessmentPossible;
    }

    public void setFullAssessmentResult(FullAssessmentResult fullAssessmentResult) {
        this.fullAssessmentResult = fullAssessmentResult;
    }

    public void setInitAssessmentResult(InitAssessmentResult initAssessmentResult) {
        this.initAssessmentResult = initAssessmentResult;
    }

}
