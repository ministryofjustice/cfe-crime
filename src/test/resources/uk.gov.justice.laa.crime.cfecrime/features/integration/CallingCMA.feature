@CallingCma
Feature: Calling CMA and passing result on to CFE Crime client

  Scenario Outline: Calling CMA Initial Means Test
    Given The Initial Means Test Details
      | caseType   | magCourtOutcome   | hasAPartner   | frequency   | income   |
      | <caseType> | <magCourtOutcome> | <hasAPartner> | <frequency> | <income> |
    When I call CFE
    Then I expect the result to be
      | higherThreshold   | lowerThreshold   | adjustedAnnualIncome   | isFullAssessmentAvailable   | outcome   |
      | <higherThreshold> | <lowerThreshold> | <adjustedAnnualIncome> | <isFullAssessmentAvailable> | <outcome> |
    Examples:
      | hasAPartner | caseType     | magCourtOutcome  | frequency | income  | outcome                        |adjustedAnnualIncome | lowerThreshold | higherThreshold | isFullAssessmentAvailable |
      | false       | SUMMARY_ONLY | RESOLVED_IN_MAGS | MONTHLY   | 1611.00 |                                | 19332.00            | 12475.00       | 22325.00        | true                      |
      | true        | SUMMARY_ONLY | RESOLVED_IN_MAGS | MONTHLY   | 1611.00 | ELIGIBLE_WITH_NO_CONTRIBUTION  | 11787.80            | 12475.00       | 22325.00        | false                     |

  Scenario Outline: Calling CMA Full Means Test
    Given The Initial Means Test Details
      | caseType   | magCourtOutcome   | hasAPartner   | frequency         | income   |
      | <caseType> | <magCourtOutcome> | <hasAPartner> | <incomeFrequency> | <income> |
    And Full Means Test Details
      | frequency           | outgoings   |
      | <outgoingFrequency> | <outgoings> |
    When I call CFE
    Then I expect the result to be
      | eligibilityThreshold   | totalAggregatedExpenditure   | disposableIncome   | totalAggregatedIncome   | adjustedLivingAllowance   |
      | <eligibilityThreshold> | <totalAggregatedExpenditure> | <disposableIncome> | <totalAggregatedIncome> | <adjustedLivingAllowance> |
    Examples:
      | hasAPartner | caseType     | magCourtOutcome  | incomeFrequency |outgoingFrequency | income  | outgoings | eligibilityThreshold | totalAggregatedExpenditure | disposableIncome | totalAggregatedIncome | adjustedLivingAllowance |
      | false       | SUMMARY_ONLY | RESOLVED_IN_MAGS | MONTHLY         |MONTHLY           | 1611.00 | 45.00     | 37500.00             | 6216.00                    | 13116.00         |     19332.00          |  5676.0000              |
      | true        | SUMMARY_ONLY | RESOLVED_IN_MAGS | MONTHLY         |MONTHLY           | 2611.00 | 45.00     | 37500.00             | 9848.64                    | 21483.36         |     31332.00          |  9308.6400              |
