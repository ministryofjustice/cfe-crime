@InitialMeansTest #@SmokeTest
Feature: InitialMeansTest
  @ValidMeansTest
  Scenario: Get the InitialMeansTest calculation
      Given the following input data
        | InitAssessmentResult | FullAssessmentResult | MagistrateCourt | CaseType     | FullAssessmentPossible |
        | PASS                 | PASS                 | [Blank]         | SUMMARY_ONLY | true                   |
        | FAIL                 | FAIL                 | COMMITTED       | SUMMARY_ONLY | true                   |
        | FAIL                 | FAIL                 | [Blank]         | APPEAL_CC    | true                   |
        | FULL                 | PASS                 | [Blank]         | SUMMARY_ONLY | true                   |
    And I call CMA
    Then I should see the following response from initMeansTest
      | InitialMeansTest              | FullMeansTest                 |
      | ELIGIBLE_WITH_NO_CONTRIBUTION | [Blank]                       |
      | [Blank]                       | [Blank]                       |
      | [Blank]                       | [Blank]                       |
      | [Blank]                       | ELIGIBLE_WITH_NO_CONTRIBUTION |
