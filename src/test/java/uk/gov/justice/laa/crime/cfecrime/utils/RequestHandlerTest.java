package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefitResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18Response;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestHandlerTest {

    private CfeCrimeRequest request = null;

    private LocalCmaService cmaService;

    private RequestHandler requestHandler = null;

    @BeforeEach
    public void init(){
        request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH);
        cmaService = new LocalCmaService(null,null, false);
        requestHandler = new RequestHandler(cmaService);
    }

    @Test
    public void ClientUnder18OutcomeIsEligible() {
        RequestTestUtil.setSectionUnder18(request,true);
        CfeCrimeResponse response = requestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
        assertThat(response.getSectionUnder18Response()).isEqualTo(new SectionUnder18Response(Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION));
    }

    @Test
    public void ClientPassportBenefitedOutcomeIsEligible() throws UndefinedOutcomeException {
        RequestTestUtil.setSectionUnder18(request,false);
        RequestTestUtil.setSectionPassportBenefit(request,true);
        CfeCrimeResponse response = requestHandler.handleRequest(request);

        assertThat(response.getOutcome()).isEqualTo(Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION);
        assertThat(response.getSectionPassportedBenefitResponse()).isEqualTo(new SectionPassportedBenefitResponse(Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION));
    }

    //Unhappy outcome
    @Test
    public void ClientNotPassportBenefitedOutcomeIsNull() throws UndefinedOutcomeException {

        RequestTestUtil.setSectionPassportBenefit(request,false);
        CfeCrimeResponse response = requestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

    @Test
    public void ClientIsNotUnder18OutcomeIsNull() throws UndefinedOutcomeException {

        RequestTestUtil.setSectionUnder18(request,false);
        CfeCrimeResponse response = requestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

    @Test
    public void ClientProvidedNothingOutcomeIsNull() throws UndefinedOutcomeException {

        CfeCrimeResponse response = requestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

    @Test
    public void ClientProvidedNothingExceptAssessmentDateOutcomeIsNull() throws UndefinedOutcomeException {

        request = new CfeCrimeRequest();
        CfeCrimeResponse response = requestHandler.handleRequest(request);

        assertEquals(response.getOutcome(), null);
    }

}
