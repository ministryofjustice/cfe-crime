package uk.gov.justice.laa.crime.cfecrime;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils.CmaResponseUtil;
import uk.gov.justice.laa.crime.cfecrime.controllers.CfeCrimeController;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CfeCrimeController.class)
class CfeControllerTest {
    @Autowired
    private MockMvc mvc;
    private static Logger log = Logger.getLogger(String.valueOf(CfeControllerTest.class));

    private ICmaService cmaService;

    final String BAD_REQUEST_ERROR = "Bad CFE Crime Request";
    @BeforeEach
    public void init(){
        cmaService = new LocalCmaService(InitAssessmentResult.FULL, FullAssessmentResult.INEL, false);
    }
    @Test
    void validJsonProducesSuccessResult() throws Exception {

        CfeCrimeRequest request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request);
        RequestTestUtil.setSectionUnder18(request,true);

        var content = RequestTestUtil.getRequestAsJson(request);

        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"outcome\":\"ELIGIBLE_WITH_NO_CONTRIBUTION\"}", response.getContentAsString());

    }

    @Test
    void validJsonProducesUnSuccessResult() throws Exception {
        CfeCrimeRequest request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request);

        var content = RequestTestUtil.getRequestAsJson(request);

        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        CfeCrimeResponse responseExpected = new CfeCrimeResponse();
        ObjectMapper objMapper = new ObjectMapper();
        assertEquals(objMapper.writeValueAsString(responseExpected), response.getContentAsString());
        assertEquals("{}", response.getContentAsString());

    }

    @Test
    void invalidJsonProducesErrorResult() throws Exception {
        var assessment = Map.of("assessment",
                Map.of("submission_date", "2023-05-02"));
        var content = new JSONObject(assessment).toString();
        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(response.getContentAsString().contains(BAD_REQUEST_ERROR),true, "Response Body contains 'Bad CFE Crime Request'");
        assertEquals(response.getContentAsString().contains("NotNull.cfeCrimeRequest.assessment.assessmentDate"),true, "Response Body contains 'NotNull.cfeCrimeRequest.assessment.assessmentDate'");

    }

    @Test
    void exceptionJsonProducesErrorResult() throws Exception {
        CfeCrimeRequest cfeCrimeRequest = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(cfeCrimeRequest);
        RequestTestUtil.setSectionInitMeansTest(cfeCrimeRequest, CaseType.APPEAL_CC, MagCourtOutcome.RESOLVED_IN_MAGS);
        RequestTestUtil.setSectionFullMeansTest(cfeCrimeRequest);
        CmaResponseUtil.setCmaResponse(cmaService, false, FullAssessmentResult.INEL, InitAssessmentResult.FULL);
        String jsonStringContent = RequestTestUtil.getRequestAsJson(cfeCrimeRequest);
        log.info("CfeCrimeRequest = "+ jsonStringContent);
        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStringContent))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(response.getContentAsString().contains(BAD_REQUEST_ERROR),true, "Response Body contains 'CFE Crime Request'");
        assertEquals(response.getContentAsString().contains("Undefined outcome for these inputs"),true, "Response Body contains 'Undefined outcome for these inputs'");

    }

    @Test
    void invalidRequestJsonProducesErrorResult() throws Exception {
        CfeCrimeRequest cfeCrimeRequest = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(cfeCrimeRequest);
        RequestTestUtil.setSectionInitMeansTestError(cfeCrimeRequest, CaseType.SUMMARY_ONLY, null);
        RequestTestUtil.setSectionFullMeansTest(cfeCrimeRequest);
        CmaResponseUtil.setCmaResponse(cmaService, false, FullAssessmentResult.INEL, InitAssessmentResult.FULL);
        String jsonStringContent = RequestTestUtil.getRequestAsJson(cfeCrimeRequest);
        log.info("CfeCrimeRequest = "+ jsonStringContent);
        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStringContent))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(response.getContentAsString().contains(BAD_REQUEST_ERROR),true, "Response Body contains 'CFE Crime Request'");
        //Timestamp is always different so assertion would not work
        //assertEquals("{"status":"BAD_REQUEST","timestamp":"2023-08-14T14:41:35.827216","message":"Bad CFE Crime Request","errors":[{"codes":null,"arguments":null,"defaultMessage":"Error in Request: [Field error in object 'cfeCrimeRequest' on field 'sectionInitialMeansTest.hasPartner': rejected value [null]; codes [NotNull.cfeCrimeRequest.sectionInitialMeansTest.hasPartner,NotNull.sectionInitialMeansTest.hasPartner,NotNull.hasPartner,NotNull.java.lang.Boolean,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [cfeCrimeRequest.sectionInitialMeansTest.hasPartner,sectionInitialMeansTest.hasPartner]; arguments []; default message [sectionInitialMeansTest.hasPartner]]; default message [must not be null], Field error in object 'cfeCrimeRequest' on field 'sectionInitialMeansTest.magistrateCourtOutcome': rejected value [null]; codes [NotNull.cfeCrimeRequest.sectionInitialMeansTest.magistrateCourtOutcome,NotNull.sectionInitialMeansTest.magistrateCourtOutcome,NotNull.magistrateCourtOutcome,NotNull.uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [cfeCrimeRequest.sectionInitialMeansTest.magistrateCourtOutcome,sectionInitialMeansTest.magistrateCourtOutcome]; arguments []; default message [sectionInitialMeansTest.magistrateCourtOutcome]]; default message [must not be null], Field error in object 'cfeCrimeRequest' on field 'sectionInitialMeansTest.caseType': rejected value [null]; codes [NotNull.cfeCrimeRequest.sectionInitialMeansTest.caseType,NotNull.sectionInitialMeansTest.caseType,NotNull.caseType,NotNull.uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [cfeCrimeRequest.sectionInitialMeansTest.caseType,sectionInitialMeansTest.caseType]; arguments []; default message [sectionInitialMeansTest.caseType]]; default message [must not be null]]","objectName":"Bad CFE Crime Request","code":null}]}", response.getContentAsString());
        assertEquals(response.getContentAsString().contains("NotNull.cfeCrimeRequest.sectionInitialMeansTest.caseType"),true, "Response Body contains 'NotNull.cfeCrimeRequest.sectionInitialMeansTest.caseType'");

    }
}