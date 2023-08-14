package uk.gov.justice.laa.crime.cfecrime.interfaces;

import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;

public interface ICmaService {
    StatelessApiResponse callCma(StatelessApiRequest request);
}
