package uk.gov.justice.laa.crime.cfecrime.controllers;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CfeCrimeError {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private List<ObjectError> errors;

    private CfeCrimeError(){
        timestamp = LocalDateTime.now();
    }

    CfeCrimeError (HttpStatus status, String message, List<ObjectError> details){
        this();
        this.status = status;
        this.message = message;
        this.errors = details;
    }

}