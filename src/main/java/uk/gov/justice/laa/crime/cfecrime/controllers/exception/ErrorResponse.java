package uk.gov.justice.laa.crime.cfecrime.controllers.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus statusCode;
    private String message;
    public ErrorResponse(String message){
        super();
        this.message=message;
    }
}
