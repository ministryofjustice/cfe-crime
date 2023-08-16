package uk.gov.justice.laa.crime.cfecrime.steps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputData {
    Outcome MeansTestOutcome;
}