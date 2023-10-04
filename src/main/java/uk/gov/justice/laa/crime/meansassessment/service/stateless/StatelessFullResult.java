package uk.gov.justice.laa.crime.meansassessment.service.stateless;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StatelessFullResult {
    private FullAssessmentResult result;
    private BigDecimal disposableIncome;
    private BigDecimal adjustedLivingAllowance;
    private BigDecimal totalAnnualAggregatedExpenditure;
    private BigDecimal eligibilityThreshold;

    public String getResultReason() {
        return result.getReason();
    }
}

