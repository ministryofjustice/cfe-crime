package uk.gov.justice.laa.crime.cfecrime;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.ConfigurationParameters;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SuiteDisplayName("Junit cucumber test suite")
@IncludeEngines("cucumber")
@ConfigurationParameters({
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "uk.gov.justice.laa.crime.cfecrime.steps"),
        @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, junit:build/test-results/TEST-cucumber.xml")
})
@SelectClasspathResource(value = "uk.gov.justice.laa.crime.cfecrime")
@SelectPackages("uk.gov.justice.laa.crime.cfecrime")
public class RunCucumberTest {
}
