package uk.gov.justice.laa.crime.cfecrime.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;

import org.mockito.InjectMocks;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.justice.laa.crime.cfecrime.controllers.CfeCrimeController;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CfeCrimeController.class)
/**
 * CFE-Crime Step definition
 * API  Scenarios and checking the response
 */
public class CfeControllerStepDefs {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    private MvcResult result; //allows to track results

    private static final String endpoint = "/v1/assessment/";
    @InjectMocks
    private CfeCrimeController cfeCrimeController;

    /**
     * Build the controller under test
    */
    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(new CfeCrimeController()).build();
    }

    /**
     * Set up the mock server up

    @Before
    public void serverSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
     */
    /**
     * Build the webApplicationContext

    @Given("CFE-Crime server is up and running")
    public void test_the_server_is_up_and_running(){
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
*/
    @When("client makes valid call \\/v1\\/assessment")
    public void client_makes_valid_call_v1_assessment() throws Exception {
        var assessment = Map.of("assessment",
                Map.of("assessment_date", "2023-05-02"));
        var content = new JSONObject(assessment).toString();
        /*
        MockHttpServletResponse response = mockMvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"under_18\":{\"outcome\":\"eligible\"}}", response.getContentAsString());

         */
        result = this.mockMvc.perform(post(endpoint).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andDo(print()).andReturn();
    }

    @Then("the client receives status code of {int}")
    public void the_client_receives_status_code_of(Integer statusCode) {
        assertEquals(result.getResponse().getStatus(), statusCode);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Then("the client receives response content")
    public void the_client_receives_response_content() throws Throwable{
        assertNotNull(result.getResponse().getContentAsString());

        assertEquals("{\"under_18\":{\"outcome\":\"eligible\"}}", result.getResponse().getContentAsString());
    }

    @When("client makes invalid call \\/v1\\/assessment")
    public void client_makes_invalid_call_v1_assessment() throws Exception {
        var assessment = Map.of("assessment",
                Map.of("submission_date", "2023-05-02"));
        var content = new JSONObject(assessment).toString();
        result = this.mockMvc.perform(post(endpoint).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andDo(print()).andReturn();
        /*
        MockHttpServletResponse response = this.mockMvc.perform(
                        post("/v1/assessment")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

         */
    }

    @Then("the client receives invalid status code of {int}")
    public void the_client_receives_invalid_status_code_of(Integer statusCode) {
        assertEquals(result.getResponse().getStatus(), statusCode);
        assertEquals(HttpStatus.BAD_REQUEST.value(),result.getResponse().getStatus());
    }

}