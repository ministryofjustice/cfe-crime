package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
/**
 * copied from crime-means-assessment project
 */
@AllArgsConstructor
@Getter
@Setter
public class FrequencyAmount {
    @Valid
    @NotNull
    @JsonProperty("frequency")
    private Frequency frequency;

    @NotNull
    @JsonProperty("amount")
    private BigDecimal amount;
}

