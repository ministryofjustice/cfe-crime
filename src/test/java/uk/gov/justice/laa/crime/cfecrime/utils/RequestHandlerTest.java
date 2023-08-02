package uk.gov.justice.laa.crime.cfecrime.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18;
import uk.gov.justice.laa.crime.cfecrime.api.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefit;
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
        Assessment assessment = new Assessment();
        Date date = new Date();

        assessment.withAssessmentDate(date.toString());
        request.setAssessment(assessment);
    }
    @Test
    public void ClientUnder18OutcomeIsEligible() {

        setSectionUnder18(true);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult().getOutcome(), Outcome.ELIGIBLE);
    }

    @Test
    public void ClientPassportBenefitedOutcomeIsEligible() {

        setSectionPassportBenefit(true);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult().getOutcome(), Outcome.ELIGIBLE);
    }

    //Unhappy outcome
    @Test
    public void ClientNotPassportBenefitedOutcomeIsNull() {

        setSectionPassportBenefit(false);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult(), null);
    }

    @Test
    public void ClientIsNotUnder18OutcomeIsNull() {

        setSectionUnder18(false);
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult(), null);
    }

    @Test
    public void ClientIsNotUnder18AndIsNotPassportBenefitedOutcomeIsNull() {

        request = new CfeCrimeRequest();
        CfeCrimeResponse response = RequestHandler.handleRequest(request);

        assertEquals(response.getResult(), null);
    }

    private void setSectionUnder18(boolean value){
        SectionUnder18 sectionUnder18 = new SectionUnder18();
        sectionUnder18.withClientUnder18(Boolean.valueOf(value));
        request.withSectionUnder18(sectionUnder18);
    }

    private void setSectionPassportBenefit(boolean value){
        SectionPassportedBenefit sectionPassportedBenefit = new SectionPassportedBenefit();
        sectionPassportedBenefit.withPassportedBenefit(Boolean.valueOf(value));
        request.withSectionPassportedBenefit(sectionPassportedBenefit);
    }
}
