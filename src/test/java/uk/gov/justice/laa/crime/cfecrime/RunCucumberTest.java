package uk.gov.justice.laa.crime.cfecrime;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:build/cucumber-report.html"},
        features = "src/test/resources"
//        glue = "cucumber.steps"
)
public class RunCucumberTest {
}
