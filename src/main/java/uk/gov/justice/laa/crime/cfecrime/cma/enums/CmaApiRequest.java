package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmaApiRequest {
    private CmaAssessment assessment;
    private Income income;
    private Outgoing outgoing;

}
