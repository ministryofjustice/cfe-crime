package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

import java.util.Objects;

@Slf4j
public class RequestHandler {

    private ICmaService cmaService = null;

    public RequestHandler(ICmaService cmaService){
        this.cmaService = Objects.requireNonNull(cmaService, "cmaService cannot be null");
    }

    public CfeCrimeResponse handleRequest(CfeCrimeRequest cfeCrimeRequest) throws UndefinedOutcomeException {
        StatelessApiResponse statelessApiResponse = null;
        StatelessApiRequest statelessApiRequest = new StatelessApiRequest();

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
        } else {
            buildCmaRequest(cfeCrimeRequest, statelessApiRequest);
            statelessApiResponse = cmaService.callCma(statelessApiRequest);
            Objects.requireNonNull(statelessApiResponse, "statelessApiResponse cannot be null");

            setInitialMeansTestOutcome(statelessApiResponse,cfeCrimeRequest, cfeCrimeResponse);
            setFullMeansTestOutcome(statelessApiResponse,cfeCrimeRequest, cfeCrimeResponse);
        }
        return cfeCrimeResponse;
    }

    private Outcome getOutcomeFromAgeAndPassportedBenefit(Boolean clientUnder18, Boolean clientPassportedBenefit) {
        Outcome outcome = null;
        if (clientUnder18 != null && clientUnder18.booleanValue()) {
            outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        }
        if (clientPassportedBenefit != null && clientPassportedBenefit.booleanValue()) {
            outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        }
        return outcome;
    }

    public void setFullMeansTestOutcome(StatelessApiResponse statelessApiResponse, CfeCrimeRequest cfeCrimeRequest, CfeCrimeResponse cfeCrimeResponse) {

        Outcome fullOutcome = null;
        CaseType caseType = null;
        MagCourtOutcome magCourtOutcome = null;
        if (statelessApiResponse.getFullMeansAssessment().getResult() != null) {

            caseType = cfeCrimeRequest.getSectionInitialMeansTest().getCaseType();
            magCourtOutcome = cfeCrimeRequest.getSectionInitialMeansTest().getMagistrateCourtOutcome();

            FullAssessmentResult statelessfullResult = statelessApiResponse.getFullMeansAssessment().getResult();
            fullOutcome = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(statelessfullResult, caseType, magCourtOutcome);
            cfeCrimeResponse.setOutcome(fullOutcome);
        }
        cfeCrimeResponse.setOutcome(fullOutcome);

    }

    public void setInitialMeansTestOutcome(StatelessApiResponse statelessApiResponse, CfeCrimeRequest cfeCrimeRequest, CfeCrimeResponse cfeCrimeResponse) throws UndefinedOutcomeException {
        CaseType caseType = null;
        MagCourtOutcome magCourtOutcome = null;

        Outcome initOutcome = null;
        if (cfeCrimeRequest.getSectionInitialMeansTest() != null) {
            caseType = cfeCrimeRequest.getSectionInitialMeansTest().getCaseType();
            if (cfeCrimeRequest.getSectionInitialMeansTest().getMagistrateCourtOutcome() != null) {
                magCourtOutcome = cfeCrimeRequest.getSectionInitialMeansTest().getMagistrateCourtOutcome();
            }
            InitAssessmentResult initAssessmentResult = statelessApiResponse.getInitialMeansAssessment().getResult();
            initOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult, statelessApiResponse.getInitialMeansAssessment().isFullAssessmentPossible());
        }
        cfeCrimeResponse.setOutcome(initOutcome);

    }

    private static void buildCmaRequest(CfeCrimeRequest cfeCrimeRequest, StatelessApiRequest statelessApiRequest){

        Assessment assessment = new Assessment();
        if (cfeCrimeRequest.getAssessment() != null) {
            assessment.setAssessmentType(cfeCrimeRequest.getAssessment().getAssessmentType());
            assessment.setAssessmentDate(cfeCrimeRequest.getAssessment().getAssessmentDate());
        }

        if (cfeCrimeRequest.getSectionInitialMeansTest() != null ) {
            if (cfeCrimeRequest.getSectionInitialMeansTest().getIncome() != null) {
                statelessApiRequest.setIncome(cfeCrimeRequest.getSectionInitialMeansTest().getIncome());
            }
            assessment.setCaseType(cfeCrimeRequest.getSectionInitialMeansTest().getCaseType());
            var dependantChildList = cfeCrimeRequest.getSectionInitialMeansTest().getDependantChildren();
            if (dependantChildList != null) {
                assessment.withDependantChildren(dependantChildList);
            }
            assessment.setEligibilityCheckRequired(false);
            assessment.setHasPartner(cfeCrimeRequest.getSectionInitialMeansTest().getHasPartner());
            assessment.setMagistrateCourtOutcome(cfeCrimeRequest.getSectionInitialMeansTest().getMagistrateCourtOutcome());
        }
        if (cfeCrimeRequest.getSectionFullMeansTest() != null ) {
            if (cfeCrimeRequest.getSectionFullMeansTest().getOutgoings() != null) {
                statelessApiRequest.setOutgoings(cfeCrimeRequest.getSectionFullMeansTest().getOutgoings());
            }
        }

        statelessApiRequest.setAssessment(assessment);
    }
}
