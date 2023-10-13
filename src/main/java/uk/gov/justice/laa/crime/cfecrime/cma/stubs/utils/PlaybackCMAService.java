package uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.config.CmaPlayback;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;

@Conditional(CmaPlayback.class)
@Service
@RequiredArgsConstructor
public class PlaybackCMAService implements ICmaService {
    @Autowired
    private final CMARecordAndPlayback recordAndPlayback;

    @Override
    public StatelessApiResponse callCma(StatelessApiRequest request) {
        return recordAndPlayback.playback(request);
    }
}

