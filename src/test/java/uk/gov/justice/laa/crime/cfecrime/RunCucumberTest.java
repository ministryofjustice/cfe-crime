package uk.gov.justice.laa.crime.cfecrime;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("Junit cucumber test suite")
@SelectClasspathResource(value = "uk.gov.justice.laa.crime.cfecrime")
@SelectPackages("uk.gov.justice.laa.crime.cfecrime")
public class RunCucumberTest {
}
