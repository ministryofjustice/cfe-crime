package uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless;

import com.fasterxml.jackson.annotation.JsonClassDescription;

@JsonClassDescription("Levels of means test to perform. INITIAL - just the Initial Means Test. BOTH - aim to do both the Initial Means Test and Full Means Test, however doing the latter will only be performed if the Initial Means Test's result is 'FULL'")
public enum StatelessRequestType {
    INITIAL,
    BOTH;
}
