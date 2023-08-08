package uk.gov.justice.laa.crime.cfecrime.interfaces;

import uk.gov.justice.laa.crime.cfecrime.api.stateless.*;

public interface ICmaService {
    StatelessApiResponse callCma(StatelessApiRequest request);

}
