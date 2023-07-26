package uk.gov.justice.laa.crime.cfecrime.interfaces;

import uk.gov.justice.laa.crime.cfecrime.api.cma.CmaApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.cma.CmaApiResponse;

public interface ICmaService {
    CmaApiResponse callCma(CmaApiRequest request);
}
