package uk.gov.justice.laa.crime.cfecrime.cma;

import uk.gov.justice.laa.crime.cfecrime.cma.enums.CmaApiRequest;
import uk.gov.justice.laa.crime.cfecrime.cma.response.ValueList;

public interface Cma {
    ValueList callCma(CmaApiRequest request);
}
