package uk.gov.justice.laa.crime.cfecrime.utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18;
import uk.gov.justice.laa.crime.cfecrime.api.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefit;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.DependantChild;
import uk.gov.justice.laa.crime.cfecrime.api.Income;
import uk.gov.justice.laa.crime.cfecrime.api.SectionInitialMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.AllowableOutgoings;

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

    public static void getSectionInitMeansTestJson(CfeCrimeRequest cfeCrimeRequest) throws JsonProcessingException {
        List<DependantChild> dcList = new ArrayList<DependantChild>();
        DependantChild dc = new DependantChild();
        dc.setAgeCategory(DependantChild.AgeCategory._12_16);
        dcList.add(dc);
        Income  income = new Income();

        SectionInitialMeansTest simt = new SectionInitialMeansTest();
        simt.setCaseType(SectionInitialMeansTest.CaseType.EITHER_WAY);
        simt.setDependantChildren(dcList);
        simt.setHasPartner(false);
        simt.setIncome(income);

    }

    public static void getSectionFullMeansTestJson(CfeCrimeRequest cfeCrimeRequest) throws JsonProcessingException {
        AllowableOutgoings aos = new AllowableOutgoings();

        SectionFullMeansTest sfmt = new SectionFullMeansTest();
        sfmt.setAdditionalProperty("Property", "Rented");
        sfmt.setAllowableOutgoings(aos);

    }

    public static void getAssementJson(CfeCrimeRequest cfeCrimeRequest) throws JsonProcessingException {
        Assessment assement = new Assessment().withAssessmentDate("2023-05-02");
        cfeCrimeRequest.setAssessment(assement);
    }

    public static String getRequestAsJson(CfeCrimeRequest cfeCrimeRequest) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(cfeCrimeRequest);
    }

}
