package uk.gov.justice.laa.crime.cfecrime;

import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Junit cucumber test suite")
//@CucumberOptions(
//        plugin = {"pretty", "html:build/reports" +
//                "/cucumber-report.html"},
//        features = "src/test/resources",
//        glue = {"uk.gov.justice.laa.crime.cfecrime.steps"}
//        //tags = "@logging"
//)
@SelectClasspathResource(value = "/features")
@SelectPackages("uk.gov.justice.laa.crime.cfecrime.steps")
public class RunCucumberTest {
}
