package uk.gov.justice.laa.crime.cfecrime.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils.CMARecordingMode;

public class CmaPlayback implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var mode = CMARecordingMode.recordingMode();

        if (mode != null) {
            return mode;
        }
        else {
            return false;
        }
    }
}
