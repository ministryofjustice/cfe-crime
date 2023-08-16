package uk.gov.justice.laa.crime.cfecrime.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UndefinedOutcomeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public UndefinedOutcomeException(InitAssessmentResult initAssessmentResult, boolean fullAssessmentAvailable){
        super("InitMeansTestOutcome: Undefined outcome for these inputs: Init Means Test. Inputs :" +
                " initAssessmentResult = " + initAssessmentResult +
                " fullAssessmentAvailable = " + fullAssessmentAvailable);
    }
}
