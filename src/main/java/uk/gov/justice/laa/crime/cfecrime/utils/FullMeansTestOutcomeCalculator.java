package uk.gov.justice.laa.crime.cfecrime.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.CaseType;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.cfecrime.cma.enums.MeansTestOutcome;

import java.util.Set;

@Slf4j
public class FullMeansTestOutcomeCalculator {

    public static MeansTestOutcome getFullMeansTestOutcome(FullAssessmentResult result, CaseType caseType, MagCourtOutcome magCourtOutcome){
        log.debug("FullMeansTestOutcome start. Inputs: result = {} caseType = {} magCourtOutcome = {}", result, caseType, magCourtOutcome);

        MeansTestOutcome meansTestOutcome = null;

        // Magistrates' court
        if (isMagistrateCourtCaseType(caseType) ||
            (caseType == CaseType.EITHER_WAY && magCourtOutcome != MagCourtOutcome.COMMITTED_FOR_TRIAL && magCourtOutcome != null)) {
                if (result == FullAssessmentResult.FAIL) {
                    meansTestOutcome = MeansTestOutcome.INELIGIBLE;
                }
                if (result == FullAssessmentResult.PASS) {
                    meansTestOutcome = MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
                }
        }

        // Crown Court
        if (isCrownCourtCaseType(caseType) ||
            (caseType == CaseType.EITHER_WAY && magCourtOutcome == MagCourtOutcome.COMMITTED_FOR_TRIAL)) {
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

    private static Set<CaseType> caseTypesHeardInMagistratesCourt = Set.of(CaseType.COMMITAL, CaseType.SUMMARY_ONLY);

    private static boolean isMagistrateCourtCaseType(CaseType caseType) {
        return caseType != null && caseTypesHeardInMagistratesCourt.contains(caseType);
    }

    private static Set<CaseType> caseTypesHeardInCrownCourt = Set.of(CaseType.INDICTABLE,
            CaseType.CC_ALREADY,
            CaseType.APPEAL_CC);

    private static boolean isCrownCourtCaseType(CaseType caseType) {
        return caseType != null && caseTypesHeardInCrownCourt.contains(caseType);
    }

    private static MeansTestOutcome crownCourtOutcome(FullAssessmentResult result) {
        switch (result) {
            case INEL:
                return MeansTestOutcome.INELIGIBLE;
            case FAIL:
                return MeansTestOutcome.ELIGIBLE_WITH_CONTRIBUTION;
            case PASS:
                return MeansTestOutcome.ELIGIBLE_WITH_NO_CONTRIBUTION;
            default:
                return null;
        }
    }

}



