package uk.gov.justice.laa.crime.cfecrime.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.api.*;
import uk.gov.justice.laa.crime.cfecrime.api.Result.Outcome;
import uk.gov.justice.laa.crime.cfecrime.controllers.CfeCrimeController;
import static org.junit.jupiter.api.Assertions.*;

public class RequestHandlerTest {

    private ObjectMapper objMapper = new ObjectMapper();
    @Test
    public void OutcomeClientUnder18() {

        SectionUnder18 s = new SectionUnder18();
        s.withClientUnder18(true);
        CfeCrimeRequest request = new CfeCrimeRequest().withSectionUnder18(s);
        CfeCrimeController api = new CfeCrimeController();
        CfeCrimeResponse response = api.invoke(request).getBody();

        assertEquals(response.getResult().getOutcome(), Outcome.ELIGIBLE);
    }

    @Test
    public void OutcomeClientPassportedBenefitedTest() {

        SectionPassportedBenefit s = new SectionPassportedBenefit();
        s.withPassportedBenefit(true);
        CfeCrimeRequest request = new CfeCrimeRequest().withSectionPassportedBenefit(s);
        CfeCrimeController api = new CfeCrimeController();
        CfeCrimeResponse response = api.invoke(request).getBody();

        assertEquals(response.getResult().getOutcome(), Outcome.ELIGIBLE);
    }

    @Test
    public void OutcomeFromNotPassportedBenefited() {

        SectionPassportedBenefit s = new SectionPassportedBenefit();
        s.withPassportedBenefit(false);
        CfeCrimeRequest request = new CfeCrimeRequest().withSectionPassportedBenefit(s);
        CfeCrimeController api = new CfeCrimeController();
        CfeCrimeResponse response = api.invoke(request).getBody();

        assertEquals(response.getResult().getOutcome(), null);
    }

    @Test
    public void OutcomeFromNotUnder18() {

        SectionUnder18 s = new SectionUnder18();
        s.withClientUnder18(false);
        CfeCrimeRequest request = new CfeCrimeRequest().withSectionUnder18(s);
        CfeCrimeController api = new CfeCrimeController();
        CfeCrimeResponse response = api.invoke(request).getBody();

        assertEquals(response.getResult().getOutcome(), null);
    }
}
