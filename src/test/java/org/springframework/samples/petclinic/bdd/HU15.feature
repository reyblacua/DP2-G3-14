Feature: Cannot create appointment for a date with another appointment
   I cannot create an appointment when the hairdresser has another one
   
  Scenario: I cannot create appointment when theres another one
    Given I am logged in the system as "owner9" with password "0wn3r"
		When I try to add a new appointment for pet "Freddy" with date "2020/07/20 20:50"
    Then I cannot create appointment because theres another one