package uk.gov.justice.laa.crime.cfecrime.utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import uk.gov.justice.laa.crime.cfecrime.api.*;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.CaseType;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.MagCourtOutcome;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.AgeRange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestTestUtil {

    public static void setAssessment(CfeCrimeRequest request){
        Assessment assessment = new Assessment();
        Date date = new Date();

        assessment.withAssessmentDate(date.toString());
        request.setAssessment(assessment);
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

    public static void setSectionInitMeansTest(CfeCrimeRequest cfeCrimeRequest, CaseType caseType, MagCourtOutcome magCourtOutcome)  {
        List<DependantChild> dcList = new ArrayList<DependantChild>();
        DependantChild dc = new DependantChild();
        dc.setAgeRange(AgeRange.SIXTEEN_TO_EIGHTEEN);
        dcList.add(dc);
        Income  income = new Income();

        SectionInitialMeansTest sectionInitialMeansTest = new SectionInitialMeansTest();
        sectionInitialMeansTest.setCaseType(caseType);
        sectionInitialMeansTest.setDependantChildren(dcList);
        sectionInitialMeansTest.setHasPartner(false);
        sectionInitialMeansTest.setIncome(income);
        sectionInitialMeansTest.setMagistrateCourtOutcome(magCourtOutcome);

        cfeCrimeRequest.setSectionInitialMeansTest(sectionInitialMeansTest);
    }

    public static void setSectionFullMeansTest(CfeCrimeRequest cfeCrimeRequest)  {
        List<DependantChild> dcList = new ArrayList<DependantChild>();
        DependantChild dc = new DependantChild();
        dc.setAgeRange(AgeRange.SIXTEEN_TO_EIGHTEEN);
        dcList.add(dc);
        Income  income = new Income();

        SectionFullMeansTest sectionFullMeansTest = new SectionFullMeansTest();

        cfeCrimeRequest.setSectionFullMeansTest(sectionFullMeansTest);
        AllowableOutgoings aos = new AllowableOutgoings();

        SectionFullMeansTest sfmt = new SectionFullMeansTest();
        sfmt.setAdditionalProperty("Property", "Rented");
        sfmt.setAllowableOutgoings(aos);
        cfeCrimeRequest.setSectionFullMeansTest(sectionFullMeansTest);
    }

    public static String getRequestAsJson(CfeCrimeRequest cfeCrimeRequest) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
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
