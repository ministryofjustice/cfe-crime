package uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

public final class CmaResponseUtil {

    public static void setCmaResponse(LocalCmaService cmaService, boolean fullAssessmentPossible, FullAssessmentResult fullAssessmentResult, InitAssessmentResult initAssessmentResult){
        cmaService.setFullAssessmentPossible(fullAssessmentPossible);
        cmaService.setFullAssessmentResult(fullAssessmentResult);
        cmaService.setInitAssessmentResult(initAssessmentResult);
    }

}
