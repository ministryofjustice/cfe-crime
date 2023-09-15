package uk.gov.justice.laa.crime.cfecrime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.stateless.StatelessApiRequest;
import uk.gov.justice.laa.crime.cfecrime.cma.stubs.LocalCmaService;
import uk.gov.justice.laa.crime.cfecrime.controllers.CfeCrimeController;
import uk.gov.justice.laa.crime.cfecrime.utils.RemoteCmaService;
import uk.gov.justice.laa.crime.cfecrime.utils.RequestTestUtil;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.FullAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.InitAssessmentResult;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CfeCrimeController.class)
class CfeCrimeControllerMockingTest {

    @Autowired
    private MockMvc mvc;

    private static final String MEANS_ASSESSMENT_ENDPOINT_URL = "/v1/assessment";

    @MockBean
    private RemoteCmaService cmaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void exceptionJsonProducesErrorResult() throws Exception {
        CfeCrimeRequest cfeCrimeRequest = new CfeCrimeRequest();
        RequestTestUtil.setAssessment(cfeCrimeRequest, StatelessRequestType.BOTH);
        RequestTestUtil.setSectionUnder18(cfeCrimeRequest,false);
        RequestTestUtil.setSectionPassportBenefit(cfeCrimeRequest, false);
        RequestTestUtil.setSectionInitMeansTest(cfeCrimeRequest, CaseType.APPEAL_CC, MagCourtOutcome.RESOLVED_IN_MAGS);
        RequestTestUtil.setSectionFullMeansTest(cfeCrimeRequest);

        var invalidResponse = new LocalCmaService(InitAssessmentResult.FULL, FullAssessmentResult.INEL, false);
        when(cmaService.callCma(any(StatelessApiRequest.class))).thenReturn(invalidResponse.callCma(null));

        String jsonStringContent = objectMapper.writeValueAsString(cfeCrimeRequest);
        MockHttpServletResponse response = mvc.perform(
                        post(MEANS_ASSESSMENT_ENDPOINT_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonStringContent))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Undefined outcome for these inputs");
    }
}