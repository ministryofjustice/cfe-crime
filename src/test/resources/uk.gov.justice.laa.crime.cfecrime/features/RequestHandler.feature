@GetOutcomeFromAgeAndPassport
Feature: Handle <18 and passported_benefit
  Scenario: validation of getOutcomeFromAgeAndPassport
    @GetOutcome @SmokeTest
    Scenario Outline: Valid outcome - get Outcome From Age And Passport benefit
      Given Age is Under Eighteen is "<under_18>" Passport benefit is "<passport_benefit>"
      Then the response will return "<outcome>"
      Examples:
        |id  |under_18|passport_benefit|outcome|
        |1  |true|false|Eligible|
        |2  |false|true|Eligible|
        |3  |true|true|Eligible|
        |4  |false|false|null|

#    @GetValidOutcome
#    Scenario Outline: valid outcome
#      Given Age is Under Eighteen is "<under_18>" Passport benefit is "<passport_benefit>"
#      Then the response will return "<outcome>"
#      Examples:
#          | outcome |
#          | Eligible |
#
#    @GetInValidOutcome
#    Scenario Outline: invalid outcome
#      Given Age is Under Eighteen is "<under_18>" Passport benefit is "<passport_benefit>"
#      Then the response will return "<outcome>"
#      Examples:
#        | outcome |
#        | null    |
