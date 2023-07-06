package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import java.util.Date;
import java.util.List;

public class CmaAssessment {
    private AssessmentType type;
    private Date date;
    private CaseType caseType;
    private MagCourtOutcome magistrateCourtOutcome;
    private boolean hasPartner;
    private boolean hasPartnerContraryInterest;
    private EmploymentStatus employmentStatus;
    private List<DependantChild> dependantChildren;
}
