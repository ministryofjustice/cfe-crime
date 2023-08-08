package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.*;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.StatelessFullResult;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.StatelessInitialResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Slf4j
@UtilityClass
public class RequestHandler {
    LocalCmaService cmaService = new LocalCmaService();
    public StatelessApiResponse statelessApiResponse = null;
    StatelessApiRequest cmaRequest = new StatelessApiRequest();

    public static CfeCrimeResponse handleRequest(CfeCrimeRequest request) throws UndefinedOutcomeException {

        Boolean under18 = null;
        Boolean passported = null;
        Outcome outcome = null;
        Outcome meansTestOutcome = null;

        if (request.getSectionUnder18() != null) {
            under18 = request.getSectionUnder18().getClientUnder18();
        }
        if (request.getSectionPassportedBenefit() != null) {
            passported = request.getSectionPassportedBenefit().getPassportedBenefit();
        }
        outcome = getOutcomeFromAgeAndPassportedBenefit(under18, passported);

        CfeCrimeResponse response = new CfeCrimeResponse();
        if (outcome != null) {
            response.setOutcome(outcome);
        }else{
            //call cma
            statelessApiResponse = cmaService.callCma(cmaRequest);
            handleCmaResponse(request, response);
        }
        return response;
    }

    private static Outcome getOutcomeFromAgeAndPassportedBenefit(Boolean clientUnder18, Boolean clientPassportedBenefit) {
        Outcome outcome = null;
        if (clientUnder18 != null && clientUnder18.booleanValue()) {
            outcome = Outcome.ELIGIBLE;
        }
        if (clientPassportedBenefit != null && clientPassportedBenefit.booleanValue()) {
            outcome = Outcome.ELIGIBLE;
        }
        return outcome;
    }

    public static void handleCmaResponse(CfeCrimeRequest request, CfeCrimeResponse cfeCrimeResponse) throws UndefinedOutcomeException {

        Outcome fullOutcome = null;
        Outcome initOutcome = null;
        CaseType caseType =  null;
        MagCourtOutcome magCourtOutcome = null;
        if (request.getSectionInitialMeansTest() != null){
            caseType =  CaseType.valueOf(request.getSectionInitialMeansTest().getCaseType().name());
            magCourtOutcome = MagCourtOutcome.valueOf(request.getSectionInitialMeansTest().getMagistrateCourtOutcome().name());
            InitAssessmentResult initAssessmentResult = statelessApiResponse.getInitialMeansAssessment().getResult();
            initOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult, statelessApiResponse.getInitialMeansAssessment().isFullAssessmentPossible());
            cfeCrimeResponse.setOutcome(initOutcome);
        }

        if (request.getSectionFullMeansTest() != null){
            FullAssessmentResult statelessInitialResult = statelessApiResponse.getFullMeansAssessment().getResult();
            fullOutcome = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(statelessInitialResult, caseType, magCourtOutcome);
            cfeCrimeResponse.setOutcome(fullOutcome);

        }

    }

    public static void setCmaResponse(boolean fullAssessmentPossible, FullAssessmentResult fullAssessmentResult, InitAssessmentResult initAssessmentResult){

        cmaService.setFullAssessmentPossible(fullAssessmentPossible);
        cmaService.setFullAssessmentResult(fullAssessmentResult);
        cmaService.setInitAssessmentResult(initAssessmentResult);
    }
}
