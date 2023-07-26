package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * copied from crime-means-assessment project
 */
@AllArgsConstructor
@Getter
public class Income implements Amount {

    @JsonProperty("income_type")
    @Valid
    @NotNull
    private IncomeType incomeType;

    @Valid
    @NotNull
    @JsonProperty("applicant")
    private FrequencyAmount applicant;

    @Valid
    @JsonProperty("partner")
    private FrequencyAmount partner;
}

