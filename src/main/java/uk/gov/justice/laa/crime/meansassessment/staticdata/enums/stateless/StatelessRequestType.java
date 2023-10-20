package uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless;

import com.fasterxml.jackson.annotation.JsonClassDescription;

@JsonClassDescription("Set to INITIAL for gross income test, or BOTH for gross/disposable income test (INITIAL and FULL in Crime parlance)")
public enum StatelessRequestType {
    INITIAL,
    BOTH;
}
