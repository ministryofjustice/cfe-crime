package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * copied from crime-means-assessment project
 */
@Getter
@AllArgsConstructor
public class Outgoing implements Amount {
    @Valid
    @NotNull
    @JsonProperty("outgoing_type")
    private OutgoingType outgoingType;

    @Valid
    @NotNull
    @JsonProperty("applicant")
    private FrequencyAmount applicant;

    @Valid
    @JsonProperty("partner")
    private FrequencyAmount partner;
}

