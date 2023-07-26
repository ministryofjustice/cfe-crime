package uk.gov.justice.laa.crime.cfecrime.cma.stubs;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CmaApiResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalCmaServiceTest {
    @Test
    void callCma() {
        CmaApiResponse cmaApiResponse = new LocalCmaService().callCma(null);

        assertNotNull(cmaApiResponse.getFullResult());
        assertEquals(InitAssessmentResult.FULL.name(), cmaApiResponse.getInitialResult().getResult().name());
        assertNotNull(cmaApiResponse.getInitialResult());
    }
}