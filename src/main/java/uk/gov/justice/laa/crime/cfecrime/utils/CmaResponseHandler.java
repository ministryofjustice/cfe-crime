package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.FullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.Result;
import uk.gov.justice.laa.crime.cfecrime.api.Result.Outcome;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.StatelessFullResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Slf4j
@UtilityClass
public class CmaResponseHandler {

    public static StatelessApiResponse handleCmaResponse(CfeCrimeRequest request, StatelessApiResponse response) throws UndefinedOutcomeException {

        CfeCrimeResponse cfeCrimeResponse = new CfeCrimeResponse();
        MeansTestOutcome meansTestOutcome = null;
        CaseType caseType =  CaseType.valueOf(request.getSectionInitialMeansTest().getCaseType().name());
        MagCourtOutcome magCourtOutcome = MagCourtOutcome.valueOf(request.getSectionInitialMeansTest().getMagistrateCourtOutcome().name());

        if (response.getFullMeansAssessment() != null){
            FullAssessmentResult result = response.getFullMeansAssessment().getResult();
            meansTestOutcome = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);

        }
        if (request.getSectionInitialMeansTest() != null){
            InitAssessmentResult result = response.getInitialMeansAssessment().getResult();
            meansTestOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(result, response.getInitialMeansAssessment().isFullAssessmentPossible());
        }

        Result result = new Result();
        result.setOutcome(Outcome.valueOf(meansTestOutcome.getOutcome()));

        return response;
    }
}
