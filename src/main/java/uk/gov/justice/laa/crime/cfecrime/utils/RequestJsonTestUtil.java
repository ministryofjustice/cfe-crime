package uk.gov.justice.laa.crime.cfecrime.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.justice.laa.crime.cfecrime.api.*;
import java.util.ArrayList;
import java.util.List;

public class RequestJsonTestUtil {

    private static CfeCrimeRequest cfeCrimeRequest = new CfeCrimeRequest();

    public static String getSectionInitMeansTestJson() throws JsonProcessingException {
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

        cfeCrimeRequest.setSectionInitialMeansTest(simt);
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(cfeCrimeRequest);
    }

    public static String getSectionFullMeansTestJson() throws JsonProcessingException {
        AllowableOutgoings aos = new AllowableOutgoings();

        SectionFullMeansTest sfmt = new SectionFullMeansTest();
        sfmt.setAdditionalProperty("Property", "Rented");
        sfmt.setAllowableOutgoings(aos);

        cfeCrimeRequest.setSectionFullMeansTest(sfmt);
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(cfeCrimeRequest);
    }

    public static String getAssementJson() throws JsonProcessingException {
        Assessment assement = new Assessment().withAssessmentDate("2023-05-02");

        cfeCrimeRequest.setAssessment(assement);
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(cfeCrimeRequest);
    }

    private RequestJsonTestUtil(){
    }
}
