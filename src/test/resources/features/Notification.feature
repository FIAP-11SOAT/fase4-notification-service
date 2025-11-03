Feature: Send notifications by email

  Scenario: Sending a valid notification email
    Given a valid notification body
    When a POST request is sent to "/v1/notification/"
    Then the FakeEmailService should receive the email