package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

import java.util.Objects;


@Slf4j
@UtilityClass
public class InitMeansTestOutcomeCalculator {

    public static Outcome getInitMeansTestOutcome(InitAssessmentResult initAssessmentResult, boolean fullAssessmentAvailable) throws UndefinedOutcomeException {

        log.debug("InitMeansTestOutcome start. Inputs: initAssessmentResult = {} fullAssessmentAvailable = {} ",
                initAssessmentResult,fullAssessmentAvailable);

        Outcome meansTestOutcome = null;
        Objects.requireNonNull(initAssessmentResult, "InitAssessmentResult cannot be null");

        if (initAssessmentResult.equals(InitAssessmentResult.FAIL) && !fullAssessmentAvailable) {
            meansTestOutcome = Outcome.INELIGIBLE;
        }else if (initAssessmentResult.equals(InitAssessmentResult.PASS)) {
            meansTestOutcome = Outcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
        } else if (!fullAssessmentAvailable) {
            throw new UndefinedOutcomeException(initAssessmentResult,fullAssessmentAvailable);
        }

        log.info("InitMeansTestOutcome end. Outputs: meansTestOutcome = {}", meansTestOutcome);
        return meansTestOutcome;
    }

}
