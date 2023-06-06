package uk.gov.justice.laa.crime.cfecrime.request;

import uk.gov.justice.laa.crime.cfecrime.request.*;

import java.util.Date;
import java.util.List;

public class CmaAssessment {
    private AssessmentType type;
    private Date date;
    private CaseType caseType;
    private CourtOutcome magistrateCourtOutcome;
    private boolean hasPartner;
    private boolean hasPartnerContraryInterest;
    private EmploymentStatus employmentStatus;
    private List<DependantChild> dependantChildren;
}
