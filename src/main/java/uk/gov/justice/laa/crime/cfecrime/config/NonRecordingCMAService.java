package uk.gov.justice.laa.crime.cfecrime.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.RemoteCmaService;

@Conditional(NotRunningTests.class)
@Service
@RequiredArgsConstructor
public class NonRecordingCMAService implements ICmaService {
    @Autowired
    private final RemoteCmaService remoteCmaService;

    @Override
    public StatelessApiResponse callCma(StatelessApiRequest request) {
        return remoteCmaService.callCma(request);
    }
}
