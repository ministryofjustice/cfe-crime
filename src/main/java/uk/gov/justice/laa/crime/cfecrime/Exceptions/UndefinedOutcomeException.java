package uk.gov.justice.laa.crime.cfecrime.Exceptions;

import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

// This is deliberately a checked exception. It is designed to be thrown and caught by the controller
// code so that the controller can produce an error result.
public class UndefinedOutcomeException extends Exception {
    public UndefinedOutcomeException(InitAssessmentResult initAssessmentResult, boolean fullAssessmentAvailable){
        super("InitMeansTestOutcome: Undefined outcome for these inputs: Init Means Test. Inputs :" +
                " initAssessmentResult = " + initAssessmentResult +
                " fullAssessmentAvailable = " + fullAssessmentAvailable);
    }
}
