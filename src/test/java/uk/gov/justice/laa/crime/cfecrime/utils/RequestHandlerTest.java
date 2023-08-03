package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.Result.Outcome;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RequestHandlerTest {

    private CfeCrimeRequest request = null;
    @BeforeEach
    public void init(){
        request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request);

    }
    @Test
    public void ClientUnder18OutcomeIsEligible() {

        RequestTestUtil.setSectionUnder18(request,true);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult().getOutcome(), Outcome.ELIGIBLE);
    }

    @Test
    public void ClientPassportBenefitedOutcomeIsEligible() {
        RequestTestUtil.setSectionPassportBenefit(request,true);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult().getOutcome(), Outcome.ELIGIBLE);
    }

    //Unhappy outcome
    @Test
    public void ClientNotPassportBenefitedOutcomeIsNull() {

        RequestTestUtil.setSectionPassportBenefit(request,false);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult(), null);
    }

    @Test
    public void ClientIsNotUnder18OutcomeIsNull() {

        RequestTestUtil.setSectionUnder18(request,false);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult(), null);
    }

    @Test
    public void ClientProvidedNothingOutcomeIsNull() {

        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult(), null);
    }

    @Test
    public void ClientProvidedNothingExceptAssessmentDateOutcomeIsNull() {

        request = new CfeCrimeRequest();
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult(), null);
    }

}
