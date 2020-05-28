Feature: Cannot make appointment for inactive hairdresser
   I cannot make an appointment for an inactive hairdresser
  
  Scenario: I cannot make an appointment for an inactive hairdresser
    Given I am logged in the system as "owner1" with password "0wn3r"
    When I enter to the page of hairdresser "George Cuarto"
    Then I cannot create an appointment for him