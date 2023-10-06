package uk.gov.justice.laa.crime.cfecrime.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTestResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionInitialMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionInitialMeansTestResponse;
import uk.gov.justice.laa.crime.cfecrime.enums.Outcome;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.FrequencyAmount;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.Income;
import uk.gov.justice.laa.crime.meansassessment.service.stateless.Outgoing;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.Frequency;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.IncomeType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.OutgoingType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CallingCMA {
    private static final String MEANS_ASSESSMENT_ENDPOINT_URL = "/v1/assessment";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CucumberRequestData requestData;

    private CfeCrimeResponse cfeCrimeResponse;

    @Given("The Initial Means Test Details")
    public void theInitialMeansTestDetails(DataTable dataTable) {
        var scenarioMap = dataTable.asMaps(String.class, String.class).get(0);

        var initMeansTest = new SectionInitialMeansTest()
                .withCaseType(CaseType.valueOf(scenarioMap.get("caseType")))
                .withMagistrateCourtOutcome(MagCourtOutcome.valueOf(scenarioMap.get("magCourtOutcome")))
                .withHasPartner(Boolean.parseBoolean(scenarioMap.get("hasAPartner")))
                .withIncome(Collections.singletonList(new Income(IncomeType.EMPLOYMENT_INCOME,
                        new FrequencyAmount(Frequency.valueOf(scenarioMap.get("frequency")),
                                new BigDecimal(scenarioMap.get("income"))), null)));

        requestData.withSectionInitialMeansTest(initMeansTest);
    }

    @Given("Full Means Test Details")
    public void fullMeansTestDetails(DataTable dataTable) {
        var scenarioMap = dataTable.asMaps(String.class, String.class).get(0);

        var fullMeansTest = new SectionFullMeansTest()
                .withOutgoings(Collections.singletonList(new Outgoing(OutgoingType.RENT_OR_MORTGAGE,
                        new FrequencyAmount(Frequency.valueOf(scenarioMap.get("frequency")),
                        new BigDecimal(scenarioMap.get("outgoings"))), null)));
        requestData.withSectionFullMeansTest(fullMeansTest);
    }

    @When("I call CFE")
    public void iCallCFE() throws Exception {
        String json = objectMapper.writeValueAsString(requestData.getCfeCrimeRequest());

        var servletResponse = mvc.perform(buildRequestGivenContent(HttpMethod.POST, json, MEANS_ASSESSMENT_ENDPOINT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        var content = servletResponse.getContentAsString();
        cfeCrimeResponse = objectMapper.readValue(content, CfeCrimeResponse.class);
    }

    private static Map<String, Consumer<Pair<BigDecimal, SectionInitialMeansTestResponse>>> initialCheckers = Map.of(
            "adjustedAnnualIncome", result_pair -> {
                assertThat(result_pair.getRight().getAdjustedAnnualIncome()).isEqualTo(result_pair.getLeft());
            },
            "lowerThreshold", result_pair -> {
                assertThat(result_pair.getRight().getLowerThreshold()).isEqualTo(result_pair.getLeft());
            },
            "higherThreshold", result_pair -> {
                assertThat(result_pair.getRight().getHigherThreshold()).isEqualTo(result_pair.getLeft());
            }
    );

    private static Map<String, Consumer<Pair<BigDecimal, SectionFullMeansTestResponse>>> fullCheckers = Map.of(
            "totalAggregatedExpenditure", result_pair -> {
                assertThat(result_pair.getRight().getTotalAnnualAggregatedExpenditure()).isEqualTo(result_pair.getLeft());
            },
            "totalAggregatedIncome", result_pair -> {
                assertThat(result_pair.getRight().getTotalAggregatedIncome()).isEqualTo(result_pair.getLeft());
            },
            "adjustedLivingAllowance", result_pair -> {
                assertThat(result_pair.getRight().getAdjustedLivingAllowance()).isEqualTo(result_pair.getLeft());
            },
            "eligibilityThreshold", result_pair -> {
                assertThat(result_pair.getRight().getEligibilityThreshold()).isEqualTo(result_pair.getLeft());
            },
            "disposableIncome", result_pair -> {
                assertThat(result_pair.getRight().getDisposableIncome()).isEqualTo(result_pair.getLeft());
            }
    );

    @Then("I expect the result to be")
    public void iExpectTheResultToBe(DataTable dataTable) {
        var resultMap = dataTable.asMaps(String.class, String.class).get(0);
        var initialResponse = cfeCrimeResponse.getSectionInitialMeansTestResponse();

        for (var item : resultMap.entrySet()) {
            if (item.getKey().equals("isFullAssessmentAvailable")) {
                assertThat(initialResponse.getFullAssessmentAvailable()).isEqualTo(Boolean.parseBoolean(item.getValue()));
            } else if (item.getKey().equals("outcome")) {
                if (item.getValue() != null) {
                    assertThat(initialResponse.getOutcome()).isEqualTo(Outcome.valueOf(item.getValue()));
                }
            } else {
                var iniCheck = initialCheckers.get(item.getKey());

                if (iniCheck != null) {
                    iniCheck.accept(new ImmutablePair<>(new BigDecimal(item.getValue()), cfeCrimeResponse.getSectionInitialMeansTestResponse()));
                } else {
                    var fullCheck = fullCheckers.get(item.getKey());
                    fullCheck.accept(new ImmutablePair<>(new BigDecimal(item.getValue()), cfeCrimeResponse.getSectionFullMeansTestResponse()));
                }
            }
        }
    }

    private static MockHttpServletRequestBuilder buildRequestGivenContent(HttpMethod method, String content, String endpointUrl) {
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.request(method, endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);
        return requestBuilder;
    }

}
