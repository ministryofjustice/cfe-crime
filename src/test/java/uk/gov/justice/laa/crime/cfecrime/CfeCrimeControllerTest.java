package uk.gov.justice.laa.crime.cfecrime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.justice.laa.crime.cfecrime.api.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTest;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    private CfeCrimeRequest request;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(springSecurityFilterChain).build();
        request = new CfeCrimeRequest();
    }

    @Test
    void under18ProducesEligibleSuccessResult() throws Exception {
        RequestTestUtil.setAssessment(request, StatelessRequestType.INITIAL, LocalDate.now());
        RequestTestUtil.setSectionUnder18(request,true);

        var response = postWithGoodResponse(request);

        assertThat(response.getContentAsString()).isEqualTo("{\"outcome\":\"ELIGIBLE_WITH_NO_CONTRIBUTION\",\"section_under_18_response\":{\"outcome\":\"ELIGIBLE_WITH_NO_CONTRIBUTION\"}}");
    }

    @Test
    void emptyJsonProducesErrorMessage() throws Exception {
        var response = postWithErrorResponse(request);

        assertThat(response).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(List.of(
                "assessment - must not be null",
                "sectionUnder18 - must not be null"));
    }

    @Test
    void allSectionsMissingReturnsErrorMessage() throws Exception {
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH, LocalDate.now());

        assertThat(postWithErrorResponse(request)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(List.of(
                "sectionUnder18 - must not be null"));
    }

    @Test
    void missingPassportedSectionReturnsError() throws Exception {
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH, LocalDate.now());
        RequestTestUtil.setSectionUnder18(request, false);

        assertThat(postWithErrorResponse(request)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(List.of(
                "sectionPassportedBenefit - must not be null"));
    }

    @Test
    void missingInitialSectionReturnsError() throws Exception {
        RequestTestUtil.setAssessment(request, StatelessRequestType.INITIAL, LocalDate.now());
        RequestTestUtil.setSectionUnder18(request, false);
        RequestTestUtil.setSectionPassportBenefit(request, false);

        assertThat(postWithErrorResponse(request)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(List.of(
                "sectionInitialMeansTest - must not be null"));
    }

    @Test
    void missingFullSectionReturnsError() throws Exception {
        RequestTestUtil.setAssessment(request, StatelessRequestType.BOTH, LocalDate.now());
        RequestTestUtil.setSectionInitMeansTest(request, CaseType.CC_ALREADY, MagCourtOutcome.APPEAL_TO_CC);

        assertThat(postWithErrorResponse(request)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(List.of(
                "sectionFullMeansTest - must not be null"));
    }

    @Test
    void missingAssessmentDateProducesError() throws Exception {
        request.withSectionUnder18(new SectionUnder18(false)).withAssessment(new Assessment());

        assertThat(postWithErrorResponse(request)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(List.of(
                "assessment.assessmentDate - must not be null"));
    }

    @Test
    void invalidInitialSectionProducesErrorResult() throws Exception {
        RequestTestUtil.setAssessment(request, StatelessRequestType.INITIAL, LocalDate.now());
        RequestTestUtil.setSectionInitMeansTestError(request, CaseType.SUMMARY_ONLY, null);

        assertThat(postWithErrorResponse(request)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(List.of(
                "sectionInitialMeansTest.magistrateCourtOutcome - must not be null",
                "sectionInitialMeansTest.hasPartner - must not be null"));
    }

    @Test
    void over18NonPassportedWithLargeIncomeCallsCmaAndReturnsIneligible() throws Exception {
        RequestTestUtil
                .setAssessment(request, StatelessRequestType.INITIAL, LocalDate.of(2023, 6, 7));
        RequestTestUtil.setSectionInitMeansTest(request, CaseType.EITHER_WAY, MagCourtOutcome.APPEAL_TO_CC);
        request.getSectionInitialMeansTest().setIncome(Arrays.asList(
                new Income(IncomeType.EMPLOYMENT_INCOME,
                        new FrequencyAmount(Frequency.ANNUALLY, BigDecimal.valueOf(25000)),
                        null)));

        var response = postWithGoodResponse(request);

        var initialResp = objectMapper.readValue(response.getContentAsString(), CfeCrimeResponse.class).getSectionInitialMeansTestResponse();

        assertThat(response.getContentAsString()).isEqualTo("{\"outcome\":\"INELIGIBLE\",\"section_initial_means_test_response\":{\"gross_household_income_annual\":25000.00,\"adjusted_annual_income\":25000.00,\"lower_threshold\":12475.00,\"higher_threshold\":22325.00,\"full_assessment_available\":false,\"outcome\":\"INELIGIBLE\"}}");
        assertThat(initialResp.getGrossHouseholdIncomeAnnual()).isEqualTo(new BigDecimal(25000).setScale(2, RoundingMode.DOWN));
    }

    @Test
    void fullAssessmentCallsCmaAndReturnsIneligible() throws Exception {
        RequestTestUtil
                .setAssessment(request, StatelessRequestType.BOTH, LocalDate.of(2023, 6, 6));
        RequestTestUtil.setSectionInitMeansTest(request, CaseType.EITHER_WAY, MagCourtOutcome.APPEAL_TO_CC);
        request.getSectionInitialMeansTest().setIncome(Arrays.asList(
                new Income(IncomeType.EMPLOYMENT_INCOME,
                        new FrequencyAmount(Frequency.ANNUALLY, BigDecimal.valueOf(14000)),
                        null)));
        request.withSectionFullMeansTest(new SectionFullMeansTest().withOutgoings(Collections.emptyList()));

        var response = postWithGoodResponse(request);

        var cfeResponse = objectMapper.readValue(response.getContentAsString(), CfeCrimeResponse.class);
        var cfeFull = cfeResponse.getSectionFullMeansTestResponse();
        assertThat(cfeResponse.getOutcome()).isEqualTo(Outcome.INELIGIBLE);
        assertThat(cfeFull.getOutcome()).isEqualTo(Outcome.INELIGIBLE);
        assertThat(cfeFull.getAdjustedLivingAllowance().setScale(0)).isEqualTo(new BigDecimal(5676));
        assertThat(cfeFull.getDisposableIncome().setScale(0)).isEqualTo(new BigDecimal(8324));
        assertThat(cfeFull.getEligibilityThreshold().setScale(0)).isEqualTo(new BigDecimal(37500));
        assertThat(cfeFull.getTotalAnnualAggregatedExpenditure().setScale(0)).isEqualTo(new BigDecimal(5676));
    }

    private MockHttpServletResponse postWithGoodResponse(CfeCrimeRequest content) throws Exception {
        return mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    private List<String> postWithErrorResponse(CfeCrimeRequest content) throws Exception {
        var response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();
        var responseJson = objectMapper.readValue(response.getContentAsString(), Map.class);
        assertThat(responseJson.get("message")).isEqualTo("Bad Request");
        return (List<String>) responseJson.get("errors");
    }
}