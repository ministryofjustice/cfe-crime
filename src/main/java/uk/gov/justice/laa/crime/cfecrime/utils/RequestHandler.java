package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.*;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Slf4j
@UtilityClass
public class RequestHandler {
    @Getter
    private LocalCmaService cmaService = new LocalCmaService();
    private StatelessApiResponse statelessApiResponse = null;
    private StatelessApiRequest cmaRequest = new StatelessApiRequest();
    private CaseType caseType =  null;
    private MagCourtOutcome magCourtOutcome = null;

    public static CfeCrimeResponse handleRequest(CfeCrimeRequest cfeCrimeRequest ) throws UndefinedOutcomeException {

        Boolean under18 = null;
        Boolean passported = null;
        Outcome outcome = null;

        if (cfeCrimeRequest.getSectionUnder18() != null) {
            under18 = cfeCrimeRequest.getSectionUnder18().getClientUnder18();
        }
        if (cfeCrimeRequest.getSectionPassportedBenefit() != null) {
            passported = cfeCrimeRequest.getSectionPassportedBenefit().getPassportedBenefit();
        }
        outcome = getOutcomeFromAgeAndPassportedBenefit(under18, passported);

        CfeCrimeResponse cfeCrimeResponse = new CfeCrimeResponse();
        if (outcome != null) {
            cfeCrimeResponse.setOutcome(outcome);
        }else{
            statelessApiResponse = cmaService.callCma(cmaRequest);

            setInitialMeansTestOutcome(cfeCrimeRequest, cfeCrimeResponse);
            if (statelessApiResponse.getInitialMeansAssessment().getResult() == InitAssessmentResult.FULL) {
                setFullMeansTestOutcome(cfeCrimeRequest, cfeCrimeResponse);
            }

        }
        return cfeCrimeResponse;
    }

    private static Outcome getOutcomeFromAgeAndPassportedBenefit(Boolean clientUnder18, Boolean clientPassportedBenefit) {
        Outcome outcome = null;
        if (clientUnder18 != null && clientUnder18.booleanValue()) {
            outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        }
        if (clientPassportedBenefit != null && clientPassportedBenefit.booleanValue()) {
            outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        }
        return outcome;
    }

    public static void setFullMeansTestOutcome(CfeCrimeRequest cfeCrimeRequest, CfeCrimeResponse cfeCrimeResponse) {

        Outcome fullOutcome = null;

        if (cfeCrimeRequest.getSectionFullMeansTest() != null){
            FullAssessmentResult statelessInitialResult = statelessApiResponse.getFullMeansAssessment().getResult();
            fullOutcome = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(statelessInitialResult, caseType, magCourtOutcome);
            cfeCrimeResponse.setOutcome(fullOutcome);
        }
        cfeCrimeResponse.setOutcome(fullOutcome);

    }

    public static void setInitialMeansTestOutcome(CfeCrimeRequest cfeCrimeRequest, CfeCrimeResponse cfeCrimeResponse) throws UndefinedOutcomeException {

        Outcome initOutcome = null;
        if (cfeCrimeRequest.getSectionInitialMeansTest() != null) {
            caseType = CaseType.valueOf(cfeCrimeRequest.getSectionInitialMeansTest().getCaseType().name());
            if (cfeCrimeRequest.getSectionInitialMeansTest().getMagistrateCourtOutcome() != null) {
                magCourtOutcome = MagCourtOutcome.valueOf(cfeCrimeRequest.getSectionInitialMeansTest().getMagistrateCourtOutcome().name());
            }
            InitAssessmentResult initAssessmentResult = statelessApiResponse.getInitialMeansAssessment().getResult();
            initOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult, statelessApiResponse.getInitialMeansAssessment().isFullAssessmentPossible());
        }
        cfeCrimeResponse.setOutcome(initOutcome);

    }

    /*
    public static void setCmaResponse(boolean fullAssessmentPossible, FullAssessmentResult fullAssessmentResult, InitAssessmentResult initAssessmentResult){
        cmaService.setFullAssessmentPossible(fullAssessmentPossible);
        cmaService.setFullAssessmentResult(fullAssessmentResult);
        cmaService.setInitAssessmentResult(initAssessmentResult);
    }
    */
}
