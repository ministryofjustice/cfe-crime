package uk.gov.justice.laa.crime.cfecrime.steps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.cfecrime.api.FullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.InitialMeansTest;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputData {
    MeansTestOutcome initialMeansTest;
    MeansTestOutcome fullMeansTest;
}