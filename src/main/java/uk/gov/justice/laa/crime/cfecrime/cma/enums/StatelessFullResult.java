package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class StatelessFullResult {
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

