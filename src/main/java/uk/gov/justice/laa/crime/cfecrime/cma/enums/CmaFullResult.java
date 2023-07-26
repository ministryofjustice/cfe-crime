package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * copied from crime-means-assessment project
 */
@RequiredArgsConstructor
@Getter
public class CmaFullResult {
    private final FullAssessmentResult result;
    private final BigDecimal disposableIncome;
    private final BigDecimal adjustedIncomeValue;
    private final BigDecimal totalAggregatedIncome;
    private final BigDecimal adjustedLivingAllowance;
    private final BigDecimal totalAnnualAggregatedExpenditure;
    private final BigDecimal eligibilityThreshold;

    public String getResultReason() {
        return result.getReason();
    }
}

