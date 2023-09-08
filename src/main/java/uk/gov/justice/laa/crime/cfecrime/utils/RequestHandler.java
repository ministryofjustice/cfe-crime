package uk.gov.justice.laa.crime.cfecrime.utils;

import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.FullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.meansassessment.model.common.stateless.DependantChild;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

import java.util.List;
import java.util.Objects;

import static uk.gov.justice.laa.crime.cfecrime.utils.FullMeansTestOutcomeCalculator.getFullMeansTestOutcome;

@Service
public class RequestHandler {

    private final ICmaService cmaService;

    public RequestHandler(ICmaService cmaService){
        this.cmaService = Objects.requireNonNull(cmaService, "cmaService cannot be null");
    }

    public CfeCrimeResponse handleRequest(CfeCrimeRequest cfeCrimeRequest) throws UndefinedOutcomeException {
        Boolean under18 = null;
        Boolean passported = null;

        if (cfeCrimeRequest.getSectionUnder18() != null) {
            under18 = cfeCrimeRequest.getSectionUnder18().getClientUnder18();
        }
        if (cfeCrimeRequest.getSectionPassportedBenefit() != null) {
            passported = cfeCrimeRequest.getSectionPassportedBenefit().getPassportedBenefit();
        }
        Outcome outcome = getOutcomeFromAgeAndPassportedBenefit(under18, passported);

        CfeCrimeResponse cfeCrimeResponse = new CfeCrimeResponse();
        if (outcome == null && under18 != null && passported != null) {
            StatelessApiResponse statelessApiResponse = cmaService.callCma(buildCmaRequest(cfeCrimeRequest));
            Objects.requireNonNull(statelessApiResponse, "statelessApiResponse cannot be null");

            setInitialMeansTestOutcome(statelessApiResponse, cfeCrimeResponse);
            setFullMeansTestOutcome(statelessApiResponse,cfeCrimeRequest, cfeCrimeResponse);
        } else {
            cfeCrimeResponse.setOutcome(outcome);
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

    private void setFullMeansTestOutcome(StatelessApiResponse statelessApiResponse, CfeCrimeRequest cfeCrimeRequest, CfeCrimeResponse cfeCrimeResponse) {
        var fullResult = statelessApiResponse.getFullMeansAssessment();
        if (fullResult != null) {
            CaseType caseType = cfeCrimeRequest.getSectionInitialMeansTest().getCaseType();
            MagCourtOutcome magCourtOutcome = cfeCrimeRequest.getSectionInitialMeansTest().getMagistrateCourtOutcome();

            Outcome fullOutcome = getFullMeansTestOutcome(fullResult.getResult(), caseType, magCourtOutcome);
            cfeCrimeResponse.setFullMeansTest(new FullMeansTest().withOutcome(fullOutcome));
            cfeCrimeResponse.setOutcome(fullOutcome);
        }
    }

    private void setInitialMeansTestOutcome(StatelessApiResponse statelessApiResponse, CfeCrimeResponse cfeCrimeResponse) throws UndefinedOutcomeException {
        Outcome initOutcome = null;
        final var initialResponse = statelessApiResponse.getInitialMeansAssessment();
        if (initialResponse != null) {
            InitAssessmentResult initAssessmentResult = initialResponse.getResult();
            initOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult, initialResponse.isFullAssessmentPossible());
        }
        cfeCrimeResponse.setOutcome(initOutcome);
    }

    private static StatelessApiRequest buildCmaRequest(CfeCrimeRequest cfeCrimeRequest){
        StatelessApiRequest statelessApiRequest = new StatelessApiRequest();
        final var assessment = cfeCrimeRequest.getAssessment();
        if (assessment != null) {
            statelessApiRequest.setAssessment(new Assessment()
                    .withAssessmentDate(assessment.getAssessmentDate())
                    .withAssessmentType(assessment.getAssessmentType())
            );
        }

        final var initialTest = cfeCrimeRequest.getSectionInitialMeansTest();
        if (initialTest != null ) {
            if (initialTest.getIncome() != null) {
                statelessApiRequest.setIncome(initialTest.getIncome());
            }
            statelessApiRequest.getAssessment().setCaseType(initialTest.getCaseType());
            List<DependantChild> dependantChildList = initialTest.getDependantChildren();
            if (dependantChildList != null) {
                statelessApiRequest.getAssessment().setDependantChildren(dependantChildList);
            }
            statelessApiRequest.getAssessment().setEligibilityCheckRequired(false);
            statelessApiRequest.getAssessment().setHasPartner(initialTest.getHasPartner());
            statelessApiRequest.getAssessment().setMagistrateCourtOutcome(initialTest.getMagistrateCourtOutcome());
        }
        final var fullMeansTest = cfeCrimeRequest.getSectionFullMeansTest();
        if (fullMeansTest != null ) {
            var outgoings = fullMeansTest.getOutgoings();
            if (outgoings != null) {
                statelessApiRequest.setOutgoings(outgoings);
            }
        }
        return statelessApiRequest;
    }
}
