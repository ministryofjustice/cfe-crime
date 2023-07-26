package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * copied from crime-means-assessment project
 */
@RequiredArgsConstructor
@Getter
@Setter
public class CmaInitialResult {
    private final InitAssessmentResult result;
    private final BigDecimal lowerThreshold;
    private final BigDecimal upperThreshold;
    private final boolean fullAssessmentPossible;

    public String getResultReason() {
        return result.getReason();
    }
}

