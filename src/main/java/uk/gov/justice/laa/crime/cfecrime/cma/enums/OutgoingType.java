package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * copied from crime-means-assessment project
 */
@Getter
@AllArgsConstructor
public enum OutgoingType {
    RENT_OR_MORTGAGE("rent_or_mortgage"),
    COUNCIL_TAX("council_tax"),
    OTHER_HOUSING_FEES("other_housing_fees"),
    TAX("tax"),
    NATIONAL_INSURANCE("national_insurance"),
    CHILDCARE_COSTS("childcare_costs"),
    MAINTENANCE_COSTS("maintenance_costs");

    @JsonValue
    @NotNull
    private final String value;
}
