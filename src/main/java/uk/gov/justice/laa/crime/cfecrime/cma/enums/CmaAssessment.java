package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Setter
@Getter
public class CmaAssessment {
    public AssessmentType type;
    public Date date;
    public CaseType caseType;
    public MagCourtOutcome magistrateCourtOutcome;
    public boolean hasPartner;
    public boolean hasPartnerContraryInterest;
    public EmploymentStatus employmentStatus;
    public List<DependantChild> dependantChildren;
}
