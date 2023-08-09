package uk.gov.justice.laa.crime.cfecrime;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.controllers.CfeCrimeController;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CfeCrimeController.class)
class CfeControllerTest {
    @Autowired
    private MockMvc mvc;

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
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        CfeCrimeResponse responseExpected = new CfeCrimeResponse();
        ObjectMapper objMapper = new ObjectMapper();
        assertEquals(objMapper.writeValueAsString(responseExpected), response.getContentAsString());

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
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void exceptionJsonProducesErrorResult() throws Exception {
        CfeCrimeRequest cfeCrimeRequest = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(cfeCrimeRequest);
        RequestTestUtil.setSectionInitMeansTest(cfeCrimeRequest, CaseType.INDICTABLE, MagCourtOutcome.COMMITTED_FOR_TRIAL);

        var content = RequestTestUtil.getRequestAsJson(cfeCrimeRequest);

        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}