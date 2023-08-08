package uk.gov.justice.laa.crime.cfecrime.steps;

import lombok.*;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputData {
    InitAssessmentResult initAssessmentResult;
    FullAssessmentResult fullAssessmentResult;
    MagCourtOutcome magCourtOutcome;
    CaseType caseType;
    boolean fullAssessmentPossible;

}