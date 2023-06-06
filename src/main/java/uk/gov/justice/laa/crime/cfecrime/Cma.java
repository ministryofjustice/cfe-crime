package uk.gov.justice.laa.crime.cfecrime;

import uk.gov.justice.laa.crime.cfecrime.request.CmaRequest;
import uk.gov.justice.laa.crime.cfecrime.response.ValueList;

public interface Cma {
    ValueList callCma(CmaRequest request);
}
