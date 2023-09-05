package uk.gov.justice.laa.crime.cfecrime.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    final String BAD_REQUEST_ERROR = "Bad CFE Crime Request";
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request){
        List<ObjectError> details = new ArrayList<>();
        ObjectError objectError = null;
        if (ex.getCause() != null && ex.getCause().getLocalizedMessage() != null) {
            objectError = new ObjectError(BAD_REQUEST_ERROR , ex.getCause().getLocalizedMessage());
        }else{
            objectError = new ObjectError(BAD_REQUEST_ERROR , ex.getLocalizedMessage());
        }

        details.add(objectError);
        CfeCrimeError cfeCrimeError = new CfeCrimeError(HttpStatus.BAD_REQUEST,BAD_REQUEST_ERROR,details);

        ResponseEntityBuilder builder = new ResponseEntityBuilder();
        return builder.build(cfeCrimeError);

    }
}
