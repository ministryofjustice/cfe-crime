package uk.gov.justice.laa.crime.cfecrime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.utils.CmaResponseUtil;
import uk.gov.justice.laa.crime.cfecrime.interfaces.ICmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.FrequencyAmount;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.Income;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.*;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.IncomeType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {
                CfeCrimeApplication.class
        }, webEnvironment = DEFINED_PORT)
class CfeCrimeControllerTest {
    private MockMvc mvc;

    private static Logger log = Logger.getLogger(String.valueOf(CfeCrimeControllerTest.class));

    private static final String MEANS_ASSESSMENT_ENDPOINT_URL = "/v1/assessment";

    private ICmaService cmaService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    final String BAD_REQUEST_ERROR = "Bad CFE Crime Request";
    @BeforeEach
    public void init(){
        cmaService = new LocalCmaService(InitAssessmentResult.FULL, FullAssessmentResult.INEL, false);
    }

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    void validJsonProducesSuccessResult() throws Exception {
        CfeCrimeRequest request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH);
        RequestTestUtil.setSectionUnder18(request,true);

        var content = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"outcome\":\"ELIGIBLE_WITH_NO_CONTRIBUTION\"}", response.getContentAsString());
    }

    @Test
    void over18NonPassportedWithLargeIncomeCallsCmaAndReturnsIneligible() throws Exception {
        CfeCrimeRequest request = RequestTestUtil
                .setAssessment(new CfeCrimeRequest(), StatelessRequestType.INITIAL);
        RequestTestUtil.setSectionUnder18(request,false);
        RequestTestUtil.setSectionPassportBenefit(request, false);
        RequestTestUtil.setSectionInitMeansTest(request, CaseType.EITHER_WAY, MagCourtOutcome.APPEAL_TO_CC);
        request.getSectionInitialMeansTest().setIncome(Arrays.asList(
                new Income(IncomeType.EMPLOYMENT_INCOME,
                        new FrequencyAmount(Frequency.ANNUALLY, BigDecimal.valueOf(25000)),
                        null)));

        var content = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"outcome\":\"INELIGIBLE\"}", response.getContentAsString());
    }

    @Test
    void fullAssessmentCallsCmaAndReturnsIneligible() throws Exception {
        CfeCrimeRequest request = RequestTestUtil
                .setAssessment(new CfeCrimeRequest(), StatelessRequestType.BOTH);
        RequestTestUtil.setSectionUnder18(request,false);
        RequestTestUtil.setSectionPassportBenefit(request, false);
        RequestTestUtil.setSectionInitMeansTest(request, CaseType.EITHER_WAY, MagCourtOutcome.APPEAL_TO_CC);
        request.getSectionInitialMeansTest().setIncome(Arrays.asList(
                new Income(IncomeType.EMPLOYMENT_INCOME,
                        new FrequencyAmount(Frequency.ANNUALLY, BigDecimal.valueOf(14000)),
                        null)));

        var content = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("{\"outcome\":\"INELIGIBLE\",\"full_means_test\":{\"outcome\":\"INELIGIBLE\"}}", response.getContentAsString());
    }

    @Test
    void validJsonProducesUnSuccessResult() throws Exception {
        CfeCrimeRequest request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH);

        var content = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        CfeCrimeResponse responseExpected = new CfeCrimeResponse();
        assertEquals(objectMapper.writeValueAsString(responseExpected), response.getContentAsString());
        assertEquals("{}", response.getContentAsString());
    }

    @Test
    void invalidJsonProducesErrorResult() throws Exception {
        var assessment = Map.of("assessment",
                Map.of("submission_date", "2023-05-02"));
        var content = objectMapper.writeValueAsString(assessment);
        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andDo(print())
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(response.getContentAsString().contains(BAD_REQUEST_ERROR),true, "Response Body contains 'Bad CFE Crime Request'");
        assertEquals(response.getContentAsString(), "{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Bad CFE Crime Request\",\"instance\":\"/v1/assessment\"}");
    }

    @Test
    void invalidRequestJsonProducesErrorResult() throws Exception {
        CfeCrimeRequest cfeCrimeRequest = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(cfeCrimeRequest, StatelessRequestType.BOTH);
        RequestTestUtil.setSectionInitMeansTestError(cfeCrimeRequest, CaseType.SUMMARY_ONLY, null);
        RequestTestUtil.setSectionFullMeansTest(cfeCrimeRequest);
        CmaResponseUtil.setCmaResponse((LocalCmaService) cmaService, false, FullAssessmentResult.INEL, InitAssessmentResult.FULL);
        String jsonStringContent = objectMapper.writeValueAsString(cfeCrimeRequest);
        log.info("CfeCrimeRequest = "+ jsonStringContent);
        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStringContent))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(response.getContentAsString().contains(BAD_REQUEST_ERROR),true, "Response Body contains 'CFE Crime Request'");
        assertEquals(response.getContentAsString(),"{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Bad CFE Crime Request\",\"instance\":\"/v1/assessment\"}");
    }
}