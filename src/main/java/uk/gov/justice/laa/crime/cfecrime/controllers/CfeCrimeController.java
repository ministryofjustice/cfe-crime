package uk.gov.justice.laa.crime.cfecrime.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandler;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;

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
    ) @RequestBody @Valid CfeCrimeRequest request, BindingResult bindingResult)  {
        if (!bindingResult.hasErrors()) {
            CfeCrimeResponse response = null;
            try {
                LocalCmaService cmaService;
                if (request.getSectionInitialMeansTest() == null) {
                    cmaService = new LocalCmaService(null, null, false);
                }else{
                    cmaService = new LocalCmaService(InitAssessmentResult.FULL, null, false);
                }
                RequestHandler requestHandler = new RequestHandler(cmaService);
                return ResponseEntity.ok(requestHandler.handleRequest(request));
            } catch (UndefinedOutcomeException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad CFE Crime Request", e);
            }
        }else{
            Exception e = new Exception("Error in Request: " + bindingResult.getAllErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad CFE Crime Request", e);
        }
    }

}