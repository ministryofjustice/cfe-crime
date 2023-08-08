@InitialMeansTest @SmokeTest
Feature: InitialMeansTest
  @ValidFullMeansTest
  Scenario: Get the InitialMeansTest calculation
      Given the following input data and I call CMA
        | InitAssessmentResult | FullAssessmentResult | MagistrateCourt | CaseType     | FullAssessmentPossible |
        | PASS                 | PASS                 | [Blank]         | SUMMARY_ONLY | true                   |
        | FAIL                 | FAIL                 | COMMITTED       | SUMMARY_ONLY | true                   |
        | FAIL                 | FAIL                 | [Blank]         | APPEAL_CC    | true                   |

    Then I should see the following results of the calculation
      | InitialMeansTest              | FullMeansTest                 |
      | ELIGIBLE_WITH_NO_CONTRIBUTION | ELIGIBLE_WITH_NO_CONTRIBUTION |
      | [Blank]                       | INELIGIBLE                    |
      | [Blank]                       | ELIGIBLE_WITH_CONTRIBUTION    |

