package uk.gov.justice.laa.crime.cfecrime.interfaces;

import uk.gov.justice.laa.crime.cfecrime.cma.enums.CmaApiRequest;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CmaApiResponse;

public interface ICmaService {
    CmaApiResponse callCma(CmaApiRequest request);
}
