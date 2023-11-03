package uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.config.CmaRecording;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;

@Conditional(CmaRecording.class)
@Service
@RequiredArgsConstructor
@Slf4j
public class RecordingCMAService implements ICmaService {
    @Autowired
    private final CMARecordAndPlayback recordAndPlayback;

    @Override
    public StatelessApiResponse callCma(StatelessApiRequest request) {
        return recordAndPlayback.record(request);
    }
}

