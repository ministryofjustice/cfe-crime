package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;

import static org.junit.jupiter.api.Assertions.*;

public class RequestHandlerTest {

    private CfeCrimeRequest request = null;
    @BeforeEach
    public void init(){
        request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request);

    }
    @Test
    public void ClientUnder18OutcomeIsEligible() throws UndefinedOutcomeException {

        RequestTestUtil.setSectionUnder18(request,true);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    @Test
    public void ClientPassportBenefitedOutcomeIsEligible() throws UndefinedOutcomeException {
        RequestTestUtil.setSectionPassportBenefit(request,true);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
    }

    //Unhappy outcome
    @Test
    public void ClientNotPassportBenefitedOutcomeIsNull() throws UndefinedOutcomeException {

        RequestTestUtil.setSectionPassportBenefit(request,false);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

    @Test
    public void ClientIsNotUnder18OutcomeIsNull() throws UndefinedOutcomeException {

        RequestTestUtil.setSectionUnder18(request,false);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

    @Test
    public void ClientProvidedNothingOutcomeIsNull() throws UndefinedOutcomeException {

        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

    @Test
    public void ClientProvidedNothingExceptAssessmentDateOutcomeIsNull() throws UndefinedOutcomeException {

        request = new CfeCrimeRequest();
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

}
