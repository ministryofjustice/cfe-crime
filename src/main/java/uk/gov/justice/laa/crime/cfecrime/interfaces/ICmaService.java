package uk.gov.justice.laa.crime.cfecrime.interfaces;

import uk.gov.justice.laa.crime.cfecrime.api.stateless.*;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

public interface ICmaService {
    StatelessApiResponse callCma(StatelessApiRequest request);

    void setFullAssessmentPossible(boolean fullAssessmentPossible);

    void setFullAssessmentResult(FullAssessmentResult fullAssessmentResult);

    void setInitAssessmentResult(InitAssessmentResult initAssessmentResult);
}
