package uk.gov.justice.laa.crime.cfecrime.Exceptions;

import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

public class UndefinedOutcomeException extends Exception {

    public UndefinedOutcomeException(InitAssessmentResult initAssessmentResult, boolean fullAssessmentAvailable){
        super("InitMeansTestOutcome: Undefined outcome for these inputs: Init Means Test. Inputs :" +
                " initAssessmentResult = " + initAssessmentResult +
                " fullAssessmentAvailable = " + fullAssessmentAvailable);
    }
}
