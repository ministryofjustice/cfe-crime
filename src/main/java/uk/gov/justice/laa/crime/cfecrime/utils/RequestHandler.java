package uk.gov.justice.laa.crime.cfecrime.utils;

import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTestResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionInitialMeansTestResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefitResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18Response;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.util.Collections;
import java.util.Objects;

import static uk.gov.justice.laa.crime.cfecrime.utils.FullMeansTestOutcomeCalculator.getFullMeansTestOutcome;

@Service
public class RequestHandler {

    private final ICmaService cmaService;

    public RequestHandler(ICmaService cmaService){
        this.cmaService = Objects.requireNonNull(cmaService, "cmaService cannot be null");
    }

    public CfeCrimeResponse handleRequest(CfeCrimeRequest cfeCrimeRequest) throws UndefinedOutcomeException {
        CfeCrimeResponse cfeCrimeResponse = new CfeCrimeResponse();
        final var under18Section = cfeCrimeRequest.getSectionUnder18();

        Boolean  under18 = under18Section.getClientUnder18();
        Outcome outcome = getOutcomeFromAge(under18);
        if (outcome != null) {
            cfeCrimeResponse.setSectionUnder18Response(new SectionUnder18Response(outcome));
            cfeCrimeResponse.setOutcome(outcome);
        } else {
            final var passportedSection = cfeCrimeRequest.getSectionPassportedBenefit();
            Boolean passported = passportedSection.getPassportedBenefit();
            outcome = getOutcomeFromPassportedBenefit(passported);
            if (outcome != null) {
                cfeCrimeResponse.setSectionPassportedBenefitResponse(new SectionPassportedBenefitResponse(outcome));
                cfeCrimeResponse.setOutcome(outcome);
            } else {
                StatelessApiResponse statelessApiResponse = cmaService.callCma(buildCmaRequest(cfeCrimeRequest));
                Objects.requireNonNull(statelessApiResponse, "statelessApiResponse cannot be null");

                setInitialMeansTestOutcome(statelessApiResponse, cfeCrimeResponse);
                setFullMeansTestOutcome(statelessApiResponse, cfeCrimeRequest, cfeCrimeResponse);
            }
        }

        return cfeCrimeResponse;
    }

    private static Outcome getOutcomeFromAge(Boolean clientUnder18) {
        Outcome outcome = null;
        if (clientUnder18 != null && clientUnder18.booleanValue()) {
            outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        }
        return outcome;
    }

    private static Outcome getOutcomeFromPassportedBenefit(Boolean clientPassportedBenefit) {
        Outcome outcome = null;
        if (clientPassportedBenefit != null && clientPassportedBenefit.booleanValue()) {
            outcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        }
        return outcome;
    }

    private static void setFullMeansTestOutcome(StatelessApiResponse statelessApiResponse, CfeCrimeRequest cfeCrimeRequest, CfeCrimeResponse cfeCrimeResponse) {
        var fullResult = statelessApiResponse.getFullMeansAssessment();
        if (fullResult != null) {
            var initialMeansTest = cfeCrimeRequest.getSectionInitialMeansTest();

            Outcome fullOutcome = getFullMeansTestOutcome(
                    fullResult.getResult(),
                    initialMeansTest.getCaseType(),
                    initialMeansTest.getMagistrateCourtOutcome()
            );
            cfeCrimeResponse.setSectionFullMeansTestResponse(
                    new SectionFullMeansTestResponse()
                            .withOutcome(fullOutcome)
                            .withDisposableIncome(fullResult.getDisposableIncome())
                            .withAdjustedLivingAllowance(fullResult.getAdjustedLivingAllowance())
                            .withTotalAnnualAggregatedExpenditure(fullResult.getTotalAnnualAggregatedExpenditure())
                            .withEligibilityThreshold(fullResult.getEligibilityThreshold()));
            cfeCrimeResponse.setOutcome(fullOutcome);
        }
    }

    private static void setInitialMeansTestOutcome(StatelessApiResponse statelessApiResponse, CfeCrimeResponse cfeCrimeResponse) throws UndefinedOutcomeException {
        final var initialResponse = statelessApiResponse.getInitialMeansAssessment();
        if (initialResponse != null) {
            InitAssessmentResult initAssessmentResult = initialResponse.getResult();
            var initialReply = new SectionInitialMeansTestResponse()
                    .withAdjustedAnnualIncome(initialResponse.getAdjustedIncomeValue())
//                    TODO: Currently not returned by CMA
                    .withWeighting(null)
                    .withFullAssessmentAvailable(initialResponse.isFullAssessmentPossible())
                    .withGrossHouseholdIncomeAnnual(initialResponse.getTotalAggregatedIncome())
                    .withLowerThreshold(initialResponse.getLowerThreshold())
                    .withHigherThreshold(initialResponse.getUpperThreshold());

            final var initOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(initAssessmentResult, initialResponse.isFullAssessmentPossible());
            if (initOutcome != null) {
                initialReply.withOutcome(initOutcome);
                cfeCrimeResponse.setOutcome(initOutcome);
            }
            cfeCrimeResponse.setSectionInitialMeansTestResponse(initialReply);
        }
    }

    private static StatelessApiRequest buildCmaRequest(CfeCrimeRequest cfeCrimeRequest){
        final var assessment = cfeCrimeRequest.getAssessment();
        final var initialTest = cfeCrimeRequest.getSectionInitialMeansTest();

        StatelessApiRequest statelessApiRequest =
                new StatelessApiRequest()
                .withAssessment(new Assessment()
                        .withEligibilityCheckRequired(false)
                        .withAssessmentType(assessment.getAssessmentType())
                        .withAssessmentDate(assessment.getAssessmentDate())
                        .withHasPartner(initialTest.getHasPartner())
                        .withCaseType(initialTest.getCaseType())
                        .withMagistrateCourtOutcome(initialTest.getMagistrateCourtOutcome())
                        .withDependantChildren(initialTest.getDependantChildren()))
                        .withIncome(initialTest.getIncome())
                        .withOutgoings(Collections.emptyList());

        final var fullMeansTest = cfeCrimeRequest.getSectionFullMeansTest();
        if (assessment.getAssessmentType() == StatelessRequestType.BOTH ) {
            statelessApiRequest.setOutgoings(fullMeansTest.getOutgoings());
        }
        return statelessApiRequest;
    }
}
