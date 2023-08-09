package uk.gov.justice.laa.crime.cfecrime.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.controllers.exception.ErrorResponse;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandler;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/assessment")
public class CfeCrimeController {
    @Operation(description = "CFE Crime")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CfeCrimeResponse.class)
            )
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> invoke(@Parameter(
            description = "CFE Crime Request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CfeCrimeRequest.class)
            )
    ) @Valid @RequestBody CfeCrimeRequest request)  {

        CfeCrimeResponse response = null;
        try {
            response = RequestHandler.handleRequest(request);
        } catch (UndefinedOutcomeException e) {
            return handleUndefinedOutcomeException( e);
        }
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(value = UndefinedOutcomeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUndefinedOutcomeException(UndefinedOutcomeException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        ResponseEntity<Object> response = new ResponseEntity<Object>(error,new HttpHeaders(),error.getStatusCode());
        return response;
    }
}