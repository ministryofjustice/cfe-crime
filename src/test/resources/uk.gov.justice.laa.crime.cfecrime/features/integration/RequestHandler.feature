@GetOutcomeFromAgeAndPassport #@SmokeTest
Feature: Client <18 and passported benefit
  Scenario: Client Eligible
    @GetValidOutcome
    Scenario Outline: Client Under 18 or Passport benefited
      Given Client Under Eighteen "<under_18>" Passport benefited "<passport_benefit>"
      Then the response will return "<outcome>"
      Examples:
        | under_18 | passport_benefit | outcome  |
        | true     | false            | Eligible |
        | false    | true             | Eligible |
        | true     | true             | Eligible |

  Scenario: Client Not Eligible
  @GetInvalidOutcome
  Scenario Outline: Client Not Under 18 or Not Passport benefited
    Given Client not Under Eighteen "<under_18>" not Passport benefited "<passport_benefit>"
    Then the response will return "<outcome>"
    Examples:
      | under_18 | passport_benefit | outcome |
      | false    | false            | [blank] |

