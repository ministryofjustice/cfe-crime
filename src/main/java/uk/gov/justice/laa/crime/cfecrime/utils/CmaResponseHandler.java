package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Slf4j
@UtilityClass
public class CmaResponseHandler {

    public static void handleCmaResponse(CfeCrimeRequest request, CfeCrimeResponse cfeCrimeResponse, StatelessApiResponse statelessApiResponse) throws UndefinedOutcomeException {

        Outcome fullOutcome = null;
        Outcome initOutcome = null;
        CaseType caseType =  CaseType.valueOf(request.getSectionInitialMeansTest().getCaseType().name());
        MagCourtOutcome magCourtOutcome = MagCourtOutcome.valueOf(request.getSectionInitialMeansTest().getMagistrateCourtOutcome().name());

        if (request.getSectionFullMeansTest() != null){
            FullAssessmentResult result = statelessApiResponse.getFullMeansAssessment().getResult();
            fullOutcome = FullMeansTestOutcomeCalculator.getFullMeansTestOutcome(result, caseType, magCourtOutcome);
            cfeCrimeResponse.setOutcome(fullOutcome);

        }
        if (request.getSectionInitialMeansTest() != null){
            InitAssessmentResult result = statelessApiResponse.getInitialMeansAssessment().getResult();
            initOutcome = InitMeansTestOutcomeCalculator.getInitMeansTestOutcome(result, statelessApiResponse.getInitialMeansAssessment().isFullAssessmentPossible());
            cfeCrimeResponse.setOutcome(initOutcome);
        }

    }
}
