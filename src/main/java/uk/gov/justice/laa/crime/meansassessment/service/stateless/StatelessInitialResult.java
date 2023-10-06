package uk.gov.justice.laa.crime.meansassessment.service.stateless;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StatelessInitialResult {
    private InitAssessmentResult result;
    private BigDecimal lowerThreshold;
    private BigDecimal upperThreshold;
    private boolean fullAssessmentPossible;
    private BigDecimal adjustedIncomeValue;

    public String getResultReason() {
        return result.getReason();
    }
}

