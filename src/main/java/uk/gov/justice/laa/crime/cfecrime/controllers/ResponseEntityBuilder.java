package uk.gov.justice.laa.crime.cfecrime.controllers;

import org.springframework.http.ResponseEntity;
public class ResponseEntityBuilder {
    public static ResponseEntity<Object> build (CfeCrimeError cfeCrimeError){
        return new ResponseEntity<>(cfeCrimeError, cfeCrimeError.getStatus());
    }
}
