package uk.gov.justice.laa.crime.cfecrime.cma.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * copied from crime-means-assessment project
 */
@AllArgsConstructor
@Getter
public class CmaApiResponse {
    private final CmaFullResult fullResult;
    private final CmaInitialResult initialResult;
}