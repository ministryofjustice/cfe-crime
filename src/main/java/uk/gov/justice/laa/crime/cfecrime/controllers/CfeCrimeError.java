package uk.gov.justice.laa.crime.cfecrime.controllers;


import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class CfeCrimeError {

    private String message;
    private List<String> errors;

    public CfeCrimeError (String message, List<String> errors){
        this.message = message;
        this.errors = errors;
    }

    public CfeCrimeError(String message) {
        this(message, Collections.emptyList());
    }
}