@login @regression
Feature: Login Functionality
  As a user of SauceDemo
  I want to be able to login with valid credentials
  So that I can access the product catalog

  Background:
    Given the user is on the login page

  @smoke @critical
  Scenario: Successful login with valid standard user credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And the user clicks the login button
    Then the user should be on the inventory page

  @negative
  Scenario: Login fails with invalid username
    When the user attempts to login with username "invalid_user" and password "secret_sauce"
    Then the login error message "Username and password do not match" should be displayed
    And the login page should still be displayed

  @negative
  Scenario: Login fails with invalid password
    When the user attempts to login with username "standard_user" and password "wrong_password"
    Then the login error message "Username and password do not match" should be displayed

  @negative
  Scenario: Locked out user cannot access the system
    When the user attempts to login with username "locked_out_user" and password "secret_sauce"
    Then the login error message "Sorry, this user has been locked out" should be displayed

  @negative
  Scenario: Login fails with empty username
    When the user attempts to login with username "" and password "secret_sauce"
    Then the login error message "Username is required" should be displayed

  @negative
  Scenario: Login fails with empty password
    When the user attempts to login with username "standard_user" and password ""
    Then the login error message "Password is required" should be displayed

  @smoke
  Scenario Outline: Multiple user types can login
    When the user enters username "<username>" and password "secret_sauce"
    And the user clicks the login button
    Then the user should be on the inventory page

    Examples:
      | username              |
      | standard_user         |
      | problem_user          |
      | performance_glitch_user |
