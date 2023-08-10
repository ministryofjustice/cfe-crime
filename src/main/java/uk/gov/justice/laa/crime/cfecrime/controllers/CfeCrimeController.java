package uk.gov.justice.laa.crime.cfecrime.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
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
    public ResponseEntity<CfeCrimeResponse> invoke(@Parameter(
            description = "CFE Crime Request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CfeCrimeRequest.class)
            )
    ) @Valid @RequestBody CfeCrimeRequest request)  {

        CfeCrimeResponse response = null;
        try {
            response = RequestHandler.handleRequest(request);
        } catch (UndefinedOutcomeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Bad CFE Crime Request", e);
        }
        return ResponseEntity.ok(response);
    }
}