@checkout @regression
Feature: End-to-End Checkout Flow
  As a logged-in user
  I want to purchase products through the checkout process
  So that I can complete my order successfully

  @smoke @critical @e2e
  Scenario: Successfully complete a full checkout with a single item
    Given the user is logged in as standard user
    When the user adds "sauce-labs-backpack" to the cart
    And the user opens the cart
    Then the cart should contain 1 item
    When the user proceeds to checkout
    And the user fills in checkout information with first name "John", last name "Doe", postal code "12345"
    Then the user should see the order overview
    And the order total should be calculated correctly
    When the user clicks the Finish button
    Then the order confirmation page should be displayed

  @smoke @critical @e2e
  Scenario: Successfully complete checkout with multiple items
    Given the user is logged in as standard user
    When the user adds "sauce-labs-backpack" to the cart
    And the user adds "sauce-labs-bike-light" to the cart
    And the user opens the cart
    Then the cart should contain 2 items
    When the user proceeds to checkout
    And the user fills in checkout information with first name "Jane", last name "Smith", postal code "90210"
    Then the user should see the order overview
    And the order total should be calculated correctly
    When the user clicks the Finish button
    Then the order confirmation page should be displayed

  @negative
  Scenario: Checkout fails when first name is missing
    Given the user is logged in as standard user
    When the user adds "sauce-labs-backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    When the user enters checkout info without first name
    Then the checkout error "First Name is required" should be displayed
