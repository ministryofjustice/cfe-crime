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
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefit;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.FrequencyAmount;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.Income;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.Frequency;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.IncomeType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {
                CfeCrimeApplication.class
        }, webEnvironment = DEFINED_PORT)
class CfeCrimeControllerTest {
    private MockMvc mvc;

    private static final String MEANS_ASSESSMENT_ENDPOINT_URL = "/v1/assessment";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    final String BAD_REQUEST_ERROR = "Bad CFE Crime Request";

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    void validJsonProducesSuccessResult() throws Exception {
        var request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH);
        RequestTestUtil.setSectionUnder18(request,true);

        var content = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"outcome\":\"ELIGIBLE_WITH_NO_CONTRIBUTION\"}");
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
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var cfeResponse = objectMapper.readValue(response.getContentAsString(), CfeCrimeResponse.class);
        assertThat(cfeResponse.getOutcome()).isEqualTo(Outcome.INELIGIBLE);
        var initialResp = cfeResponse.getSectionInitialMeansTestResponse();
        assertThat(initialResp.getOutcome()).isEqualTo(Outcome.INELIGIBLE);
//        TODO: weighting still not returned correctly by CMA
//        assertThat(initialResp.getWeighting()).isEqualTo(new BigDecimal(2));
        assertThat(initialResp.getLowerThreshold()).isEqualTo(new BigDecimal(12475).setScale(2));
        assertThat(initialResp.getHigherThreshold()).isEqualTo(new BigDecimal(22325).setScale(2));
        assertThat(initialResp.getAdjustedAnnualIncome()).isEqualTo(new BigDecimal(25000).setScale(2));
    }

    @Test
    void fullAssessmentCallsCmaAndReturnsIneligible() throws Exception {
        var request = RequestTestUtil
                .setAssessment(new CfeCrimeRequest()
                        .withSectionUnder18(new SectionUnder18(false))
                        .withSectionPassportedBenefit(new SectionPassportedBenefit(false)), StatelessRequestType.BOTH);
        RequestTestUtil.setSectionInitMeansTest(request, CaseType.EITHER_WAY, MagCourtOutcome.APPEAL_TO_CC);
        request.getSectionInitialMeansTest().setIncome(Arrays.asList(
                new Income(IncomeType.EMPLOYMENT_INCOME,
                        new FrequencyAmount(Frequency.ANNUALLY, BigDecimal.valueOf(14000)),
                        null)));
        request.withSectionFullMeansTest(new SectionFullMeansTest().withOutgoings(Collections.emptyList()));

        var content = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var cfeResponse = objectMapper.readValue(response.getContentAsString(), CfeCrimeResponse.class);
        var cfeFull = cfeResponse.getSectionFullMeansTestResponse();
        assertThat(cfeResponse.getOutcome()).isEqualTo(Outcome.INELIGIBLE);
        assertThat(cfeFull.getOutcome()).isEqualTo(Outcome.INELIGIBLE);
        assertThat(cfeFull.getAdjustedLivingAllowance()).isEqualTo(new BigDecimal(5676).setScale(4, RoundingMode.DOWN));
        assertThat(cfeFull.getTotalAggregatedIncome()).isEqualTo(new BigDecimal(14000).setScale(2, RoundingMode.DOWN));
        assertThat(cfeFull.getDisposableIncome()).isEqualTo(new BigDecimal(8324).setScale(2, RoundingMode.DOWN));
        assertThat(cfeFull.getEligibilityThreshold()).isEqualTo(new BigDecimal(37500).setScale(2, RoundingMode.DOWN));
        assertThat(cfeFull.getTotalAnnualAggregatedExpenditure()).isEqualTo(new BigDecimal(5676).setScale(2, RoundingMode.DOWN));
    }

    @Test
    void validJsonProducesUnSuccessResult() throws Exception {
        var request = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH);

        var content = objectMapper.writeValueAsString(request);

        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{}");
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
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains(BAD_REQUEST_ERROR);
        assertThat(response.getContentAsString()).isEqualTo("{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Bad CFE Crime Request\",\"instance\":\"/v1/assessment\"}");
    }

    @Test
    void invalidRequestJsonProducesErrorResult() throws Exception {
        var cfeCrimeRequest = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(cfeCrimeRequest, StatelessRequestType.BOTH);
        RequestTestUtil.setSectionInitMeansTestError(cfeCrimeRequest, CaseType.SUMMARY_ONLY, null);
        RequestTestUtil.setSectionFullMeansTest(cfeCrimeRequest);

        String jsonStringContent = objectMapper.writeValueAsString(cfeCrimeRequest);
        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStringContent))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains(BAD_REQUEST_ERROR);
        assertThat(response.getContentAsString()).isEqualTo("{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Bad CFE Crime Request\",\"instance\":\"/v1/assessment\"}");
    }
}