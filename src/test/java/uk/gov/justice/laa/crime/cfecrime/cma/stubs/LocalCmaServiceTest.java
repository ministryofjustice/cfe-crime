package uk.gov.justice.laa.crime.cfecrime.cma.stubs;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.*;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalCmaServiceTest {
    @Test
    void callCma() {
        StatelessApiRequest request = null;
        InitAssessmentResult initAssessmentResult = InitAssessmentResult.FULL;
        FullAssessmentResult fullAssessmentResult = FullAssessmentResult.INEL;
        boolean fullAssessmentPossible = false;
        LocalCmaService cmaService = new LocalCmaService(initAssessmentResult,fullAssessmentResult,fullAssessmentPossible);

        StatelessApiResponse cmaApiResponse = cmaService.callCma(request);

        assertNotNull(cmaApiResponse.getFullMeansAssessment());
        assertNotNull(cmaApiResponse.getInitialMeansAssessment());
        assertEquals(InitAssessmentResult.FULL, cmaApiResponse.getInitialMeansAssessment().getResult());

    }
}