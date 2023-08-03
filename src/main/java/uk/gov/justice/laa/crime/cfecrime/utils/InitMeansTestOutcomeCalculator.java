package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;


@Slf4j
@UtilityClass
public class InitMeansTestOutcomeCalculator {

    public static MeansTestOutcome getInitMeansTestOutcome(InitAssessmentResult initAssessmentResult, boolean fullAssessmentAvailable) throws UndefinedOutcomeException {
        log.debug("InitMeansTestOutcome start. Inputs: initAssessmentResult = {} fullAssessmentAvailable = {} ");

        MeansTestOutcome meansTestOutcome = null;

        if (initAssessmentResult == null) {
            throw new RuntimeException("InitMeansTestOutcome: Undefined outcome for these inputs: Init Means Test " +
                    " initAssessmentResult = " + initAssessmentResult + " fullAssessmentAvailable = " + fullAssessmentAvailable);
        } else {
            if (initAssessmentResult.equals(InitAssessmentResult.FAIL) && !fullAssessmentAvailable) {
                meansTestOutcome = MeansTestOutcome.INELIGIBLE;
            }else if (initAssessmentResult.equals(InitAssessmentResult.PASS)) {
                meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
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
