package uk.gov.justice.laa.crime.cfecrime.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.cfecrime.Exceptions.UndefinedOutcomeException;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefit;
import uk.gov.justice.laa.crime.cfecrime.utils.RemoteCmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestHandler;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/assessment")
@RequiredArgsConstructor
public class CfeCrimeController {

    private static final String BAD_REQUEST = "Bad Request";
    private final RemoteCmaService cmaService;

    @Operation(description = "CFE Crime")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CfeCrimeResponse.class)
            )
    )
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CfeCrimeError.class)
            )
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity invoke(@Parameter(
            description = "CFE Crime Request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CfeCrimeRequest.class)
            )
    )
    @RequestBody @Valid CfeCrimeRequest request, BindingResult bindingResult)  {
        if (!bindingResult.hasFieldErrors()) {
            if (!request.getSectionUnder18().getClientUnder18()) {
                SectionPassportedBenefit sectionPassportedBenefit = request.getSectionPassportedBenefit();
                if (sectionPassportedBenefit == null) {
                    return ResponseEntity.badRequest().body(new CfeCrimeError(BAD_REQUEST, Arrays.asList("sectionPassportedBenefit - must not be null")));
                } else if (!sectionPassportedBenefit.getPassportedBenefit() && request.getSectionInitialMeansTest() == null) {
                    return ResponseEntity.badRequest().body(new CfeCrimeError(BAD_REQUEST, Arrays.asList("sectionInitialMeansTest - must not be null")));
                } else if (request.getAssessment().getAssessmentType() == StatelessRequestType.BOTH && request.getSectionFullMeansTest() == null) {
                    return ResponseEntity.badRequest().body(new CfeCrimeError(BAD_REQUEST, Arrays.asList("sectionFullMeansTest - must not be null")));
                } else {
                    return invokeRequestHandler(request);
                }
            } else
            {
                return invokeRequestHandler(request);
            }
        } else{
            var errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getField() + " - " + fieldError.getDefaultMessage()).toList();

            return ResponseEntity.badRequest().body(new CfeCrimeError(BAD_REQUEST, errors));
        }
    }

    private ResponseEntity invokeRequestHandler(CfeCrimeRequest request) {
        RequestHandler requestHandler = new RequestHandler(cmaService);
        try {
            return ResponseEntity.ok(requestHandler.handleRequest(request));
        } catch (UndefinedOutcomeException e) {
            return ResponseEntity.badRequest().body(new CfeCrimeError(e.getMessage()));
        }
    }
}