package uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.utils.FullMeansTestOutcomeCalculator;
import uk.gov.justice.laa.crime.cfecrime.utils.InitMeansTestOutcomeCalculator;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Slf4j
@UtilityClass
public class CmaResponseUtil {


    public static void setCmaResponse(LocalCmaService cmaService, boolean fullAssessmentPossible, FullAssessmentResult fullAssessmentResult, InitAssessmentResult initAssessmentResult){
        cmaService.setFullAssessmentPossible(fullAssessmentPossible);
        cmaService.setFullAssessmentResult(fullAssessmentResult);
        cmaService.setInitAssessmentResult(initAssessmentResult);
    }

}
