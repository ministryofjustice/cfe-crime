package uk.gov.justice.laa.crime.cfecrime.steps;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.cfecrime.api.Assessment;
import uk.gov.justice.laa.crime.cfecrime.api.CfeCrimeRequest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionFullMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionInitialMeansTest;
import uk.gov.justice.laa.crime.cfecrime.api.SectionPassportedBenefit;
import uk.gov.justice.laa.crime.cfecrime.api.SectionUnder18;
import uk.gov.justice.laa.crime.meansassessment.staticdata.enums.stateless.StatelessRequestType;

import java.time.LocalDate;

@Component
@ScenarioScope
public class CucumberRequestData {
    private CfeCrimeRequest cfeCrimeRequest;

    public CucumberRequestData() {
        cfeCrimeRequest = new CfeCrimeRequest()
                .withAssessment(
                        new Assessment()
                        .withAssessmentDate(LocalDate.now().toString()))
                .withSectionUnder18(new SectionUnder18(false))
                .withSectionPassportedBenefit(new SectionPassportedBenefit(false));
    }

    public CucumberRequestData withSectionInitialMeansTest(SectionInitialMeansTest initMeansTest) {
        cfeCrimeRequest.getAssessment().setAssessmentType(StatelessRequestType.INITIAL);
        cfeCrimeRequest.withSectionInitialMeansTest(initMeansTest);
        return this;
    }

    public CfeCrimeRequest getCfeCrimeRequest() {
        return cfeCrimeRequest;
    }

    public CucumberRequestData withSectionFullMeansTest(SectionFullMeansTest fullMeansTest) {
        cfeCrimeRequest.getAssessment().setAssessmentType(StatelessRequestType.BOTH);
        cfeCrimeRequest.withSectionFullMeansTest(fullMeansTest);
        return this;
    }
}
