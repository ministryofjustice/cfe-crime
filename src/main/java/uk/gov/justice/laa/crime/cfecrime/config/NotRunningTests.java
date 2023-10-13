package uk.gov.justice.laa.crime.cfecrime.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class NotRunningTests implements Condition {

    private final CmaRecording cmaRecording;
    private final CmaPlayback cmaPlayback;

    public NotRunningTests() {
        cmaRecording = new CmaRecording();
        cmaPlayback = new CmaPlayback();
    }
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !cmaRecording.matches(context, metadata) && !cmaPlayback.matches(context, metadata);
    }
}
