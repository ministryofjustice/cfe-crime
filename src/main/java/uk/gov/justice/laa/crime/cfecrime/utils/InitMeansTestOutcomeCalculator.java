package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;


@Slf4j
@UtilityClass
public class InitMeansTestOutcomeCalculator {

    public static Outcome getInitMeansTestOutcome(InitAssessmentResult initAssessmentResult, boolean fullAssessmentAvailable) throws UndefinedOutcomeException {

        log.debug("InitMeansTestOutcome start. Inputs: initAssessmentResult = {} fullAssessmentAvailable = {} ");

        Outcome meansTestOutcome = null;

        if (initAssessmentResult == null) {
            throw new RuntimeException("InitMeansTestOutcome: Undefined outcome for these inputs: Init Means Test " +
                    " initAssessmentResult = " + initAssessmentResult + " fullAssessmentAvailable = " + fullAssessmentAvailable);
        } else {
            if (initAssessmentResult.equals(InitAssessmentResult.FAIL) && !fullAssessmentAvailable) {
                meansTestOutcome = Outcome.INELIGIBLE;
            }else if (initAssessmentResult.equals(InitAssessmentResult.PASS)) {
                meansTestOutcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
            } else if (!fullAssessmentAvailable) {
                throw new UndefinedOutcomeException("InitMeansTestOutcome: Undefined outcome for these inputs: Init Means Test " +
                        " initAssessmentResult = " + initAssessmentResult +
                        " fullAssessmentAvailable = " + fullAssessmentAvailable);
            }
        }

        log.info("InitMeansTestOutcome end. Outputs: meansTestOutcome = {}", meansTestOutcome);
        return meansTestOutcome;
    }

}
