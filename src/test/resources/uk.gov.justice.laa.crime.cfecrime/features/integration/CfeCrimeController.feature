
Feature: the assessment  can be retreived
  As a cfe-Crime API user
  I want to know which assesment is exposed
  in order to be a good API user

    Scenario: client makes valid call to POST /v1/assessment
    When client makes valid call /v1/assessment
    Then the client receives status code of 200
    And the client receives response content

  Scenario: client makes invalid call to POST /v1/assessment
    When client makes invalid call /v1/assessment
    Then the client receives invalid status code of 400




