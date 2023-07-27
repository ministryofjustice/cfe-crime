package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.api.Under18;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

import static org.junit.jupiter.api.Assertions.*;

public class RequestHandlerTest {

    @Test
    public void OutcomeFromAgeTest() {

        Under18.Outcome oc = RequestHandlerUtil.getOutcomeFromAgeAndPassport(true, false);

        assertEquals(oc, Under18.Outcome.ELIGIBLE);
    }

    @Test
    public void OutcomeFromPassportTest() {

        Under18.Outcome oc = RequestHandlerUtil.getOutcomeFromAgeAndPassport(true, true);

        assertEquals(oc, Under18.Outcome.ELIGIBLE);
    }

    @Test
    public void OutcomeFromNeitherAgePassportTest() {

        Under18.Outcome oc = RequestHandlerUtil.getOutcomeFromAgeAndPassport(false, false);

        assertEquals(oc, null);
    }
}
