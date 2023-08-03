package uk.gov.justice.laa.crime.cfecrime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
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

        ObjectMapper objMapper = new ObjectMapper();
        var content = objMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"result\":{\"outcome\":\"eligible\"}}", response.getContentAsString());

    }

    @Test
    void validJsonProducesUnSuccessResult() throws Exception {
        CfeCrimeRequest request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request);

        ObjectMapper objMapper = new ObjectMapper();
        var content = objMapper.writeValueAsString(request);
        MockHttpServletResponse response = mvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        CfeCrimeResponse responseExpected = new CfeCrimeResponse();
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
}