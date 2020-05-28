Feature: Can see hairdressers
   I can see hairdressers list to select them to create a appointment
  
  Scenario: I can see hairdressers list
    Given I am logged in the system as "owner1" with password "0wn3r"
    When I enter to hairdressers list
    Then I see hairdressers