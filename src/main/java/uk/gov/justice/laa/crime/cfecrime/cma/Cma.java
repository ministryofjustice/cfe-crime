package uk.gov.justice.laa.crime.cfecrime.cma;

import uk.gov.justice.laa.crime.cfecrime.cma.enums.CmaRequest;
import uk.gov.justice.laa.crime.cfecrime.cma.response.ValueList;

public interface Cma {
    ValueList callCma(CmaRequest request);
}
