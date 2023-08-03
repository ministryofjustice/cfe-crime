package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.api.Under18;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

import static org.junit.jupiter.api.Assertions.*;

//Ignoring for now and will remove
//as it has been replaced by cucumber test /steps/StepDefinition and RequestHandler.feature.
public class RequestHandlerTest {

    @Ignore @Test
    public void OutcomeFromAgeTest() {

        Under18.Outcome oc = RequestHandlerUtil.getOutcomeFromAgeAndPassport(true, false);

        assertEquals(oc, Under18.Outcome.ELIGIBLE);
    }

    @Ignore @Test
    public void OutcomeFromPassportTest() {

        Under18.Outcome oc = RequestHandlerUtil.getOutcomeFromAgeAndPassport(true, true);

        assertEquals(oc, Under18.Outcome.ELIGIBLE);
    }

    @Ignore @Test
    public void OutcomeFromNeitherAgePassportTest() {

        Under18.Outcome oc = RequestHandlerUtil.getOutcomeFromAgeAndPassport(false, false);

        assertEquals(oc, null);
    }
}
