package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class StatelessInitialResult {
    private final InitAssessmentResult result;
    private final BigDecimal lowerThreshold;
    private final BigDecimal upperThreshold;
    private final boolean fullAssessmentPossible;

    public String getResultReason() {
        return result.getReason();
    }
}

