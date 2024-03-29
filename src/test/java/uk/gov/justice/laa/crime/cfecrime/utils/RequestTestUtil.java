package uk.gov.justice.laa.crime.cfecrime.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.gov.justice.laa.crime.cfecrime.api.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeResponse;
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionInitialMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefit;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.time.LocalDate;
import java.util.Collections;

public class RequestTestUtil {

    public static CfeCrimeRequest setAssessment(CfeCrimeRequest request, StatelessRequestType requestType, LocalDate date){
        return request.withAssessment(new Assessment()
                .withAssessmentDate(date.toString())
                .withAssessmentType(requestType));
    }

    public static void setSectionUnder18(CfeCrimeRequest request, boolean value){
        SectionUnder18 sectionUnder18 = new SectionUnder18();
        sectionUnder18.withClientUnder18(Boolean.valueOf(value));
        request.withSectionUnder18(sectionUnder18);
    }

    public static void setSectionPassportBenefit(CfeCrimeRequest request, boolean value){
        SectionPassportedBenefit sectionPassportedBenefit = new SectionPassportedBenefit();
        sectionPassportedBenefit.withPassportedBenefit(Boolean.valueOf(value));
        request.withSectionPassportedBenefit(sectionPassportedBenefit);
    }

    public static CfeCrimeRequest setSectionInitMeansTest(CfeCrimeRequest cfeCrimeRequest, CaseType caseType, MagCourtOutcome magCourtOutcome)  {
        SectionInitialMeansTest sectionInitialMeansTest =
                new SectionInitialMeansTest()
                        .withCaseType(caseType)
                        .withHasPartner(false)
                        .withMagistrateCourtOutcome(magCourtOutcome)
                        .withDependantChildren(Collections.emptyList())
                        .withIncome(Collections.emptyList());
        return cfeCrimeRequest
                .withSectionUnder18(new SectionUnder18(false))
                .withSectionPassportedBenefit(new SectionPassportedBenefit(false))
                .withSectionInitialMeansTest(sectionInitialMeansTest);
    }

    public static void setSectionInitMeansTestError(CfeCrimeRequest cfeCrimeRequest, CaseType caseType, MagCourtOutcome magCourtOutcome)  {

        SectionInitialMeansTest sectionInitialMeansTest = new SectionInitialMeansTest()
                .withCaseType(caseType)
                .withMagistrateCourtOutcome(magCourtOutcome);
        cfeCrimeRequest
                .withSectionUnder18(new SectionUnder18(false))
                .withSectionPassportedBenefit(new SectionPassportedBenefit(false))
                .setSectionInitialMeansTest(sectionInitialMeansTest);
    }

    public static void setSectionFullMeansTest(CfeCrimeRequest cfeCrimeRequest)  {
        SectionFullMeansTest sectionFullMeansTest = new SectionFullMeansTest();
        cfeCrimeRequest.setSectionFullMeansTest(sectionFullMeansTest);
        cfeCrimeRequest.setSectionFullMeansTest(sectionFullMeansTest);
    }

    public static String getRequestAsJson(CfeCrimeRequest cfeCrimeRequest) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        obj.registerModule(new JavaTimeModule());
        return obj.writeValueAsString(cfeCrimeRequest);
    }

    public static String getCfeCrimeRequestAsJsonString(CfeCrimeRequest cfeCrimeRequest) throws JsonProcessingException {
        String jsonString = null;

        JsonMapper map = new JsonMapper();
        map.writeValueAsString(cfeCrimeRequest);
        return jsonString;
    }

    public static String getCfeCrimeResponseAsJsonString(CfeCrimeResponse cfeCrimeResponse) throws JsonProcessingException {
        String jsonString = null;

        JsonMapper map = new JsonMapper();
        map.writeValueAsString(cfeCrimeResponse);
        return jsonString;
    }

    public static CfeCrimeRequest getCfeCrimeRequestAsJsonString(String jsonString) throws JsonProcessingException {
        CfeCrimeRequest cfeCrimeRequest;

        JsonMapper map = new JsonMapper();
        cfeCrimeRequest = map.readValue(jsonString, CfeCrimeRequest.class);
        return cfeCrimeRequest;
    }

}
