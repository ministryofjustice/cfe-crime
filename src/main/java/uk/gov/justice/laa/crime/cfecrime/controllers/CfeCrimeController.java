package uk.gov.justice.laa.crime.cfecrime.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.utils.RemoteCmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandler;

@RestController
@RequestMapping("/v1/assessment")
@RequiredArgsConstructor
public class CfeCrimeController {

    private final RemoteCmaService cmaService;

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
    ) @RequestBody @Valid CfeCrimeRequest request, BindingResult bindingResult)  {
        if (!bindingResult.hasErrors()) {
            RequestHandler requestHandler = new RequestHandler(cmaService);
            return ResponseEntity.ok(requestHandler.handleRequest(request));
        }else{
            Exception e = new Exception("Error in Request: " + bindingResult.getAllErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad CFE Crime Request", e);
        }

    }

}