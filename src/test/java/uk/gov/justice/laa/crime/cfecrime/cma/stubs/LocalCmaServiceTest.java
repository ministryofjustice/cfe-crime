package uk.gov.justice.laa.crime.cfecrime.cma.stubs;
import uk.gov.justice.laa.crime.cfecrime.api.*;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.api.cma.*;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.InitAssessmentResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalCmaServiceTest {
    @Test
    void callCma() {
        CmaApiRequest request = null;
        CmaApiResponse cmaApiResponse = new LocalCmaService().callCma(request);

        assertNotNull(cmaApiResponse.getFullMeansAssessment());
        assertNotNull(cmaApiResponse.getInitialMeansAssessment());
        assertEquals(InitAssessmentResult.FULL.name(), cmaApiResponse.getInitialMeansAssessment().getResult().name());

    }
}