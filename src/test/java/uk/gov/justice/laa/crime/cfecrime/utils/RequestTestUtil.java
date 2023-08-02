package uk.gov.justice.laa.crime.cfecrime.utils;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18;
import uk.gov.justice.laa.crime.cfecrime.api.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefit;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;

import java.util.Date;

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
}
