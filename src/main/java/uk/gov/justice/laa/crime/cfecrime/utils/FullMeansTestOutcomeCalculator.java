package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;

import java.util.Map;
import java.util.Set;

@Slf4j
public class FullMeansTestOutcomeCalculator {

    public static MeansTestOutcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("FullMeansTestOutcome start. Inputs: result = {} caseType = {} magCourtOutcome = {}", result, caseType, magCourtOutcome);

        MeansTestOutcome meansTestOutcome = null;

        if (isMagistrateCourtCaseType(caseType, magCourtOutcome)) {
            if (result == FullAssessmentResult.FAIL) {
                meansTestOutcome = MeansTestOutcome.INELIGIBLE;
            }
            if (result == FullAssessmentResult.PASS) {
                meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
            }
        }

        if (isCrownCourtCaseType(caseType, magCourtOutcome)) {
            meansTestOutcome = crownCourtOutcome(result);
        }

        if (meansTestOutcome == null) {
            throw new RuntimeException("FullMeansTestOutcome: Undefined outcome for these inputs: Full Means Test result = " + result +
                                       " caseType = " + caseType + " magCourtOutcome = " + magCourtOutcome);
        }
        log.info("FullMeansTestOutcome end. Outputs: meansTestOutcome = {}", meansTestOutcome);
        return meansTestOutcome;
    }

    private FullMeansTestOutcomeCalculator(){
    }

    private static boolean isMagistrateCourtCaseType(CaseType caseType, MagCourtOutcome magCourtOutcome) {
        if (caseType != null) {
            switch (caseType) {
                case COMMITAL:
                case SUMMARY_ONLY:
                    return true;
                case EITHER_WAY:
                    return magCourtOutcome != MagCourtOutcome.COMMITTED_FOR_TRIAL && magCourtOutcome != null;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private static Set<CaseType> caseTypesHeardInCrownCourt = Set.of(
            CaseType.INDICTABLE,
            CaseType.CC_ALREADY,
            CaseType.APPEAL_CC
    );

    private static boolean isCrownCourtCaseType(CaseType caseType, MagCourtOutcome magCourtOutcome) {
        if (caseType != null && caseTypesHeardInCrownCourt.contains(caseType)) {
            return true;
        } else if (caseType == CaseType.EITHER_WAY && magCourtOutcome == MagCourtOutcome.COMMITTED_FOR_TRIAL) {
            return true;
        } else {
            return false;
        }
    }

    private static Map<FullAssessmentResult, MeansTestOutcome> crownCourtOutcomes =
            Map.of(FullAssessmentResult.INEL, MeansTestOutcome.INELIGIBLE,
                    FullAssessmentResult.FAIL, MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION,
                    FullAssessmentResult.PASS, MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION);

    private static MeansTestOutcome crownCourtOutcome(FullAssessmentResult result) {
        return crownCourtOutcomes.get(result);
    }

}



