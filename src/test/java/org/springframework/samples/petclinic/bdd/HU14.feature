Feature: Cannot delete appointment which is today
   I cannot delete todays appointments
   
  Scenario: I can delete appointment which is not today
    Given I am logged in the system as "owner7" with password "0wn3r"
	  When I add a new appointment for pet "Lucky" with date "2020/08/09 20:00"
    Then I can delete appointment which is not today
    
  Scenario: I cannot delete todays appointment
    Given I am logged in the system as "owner8" with password "0wn3r"
    When I create an appointment for today for pet "Mulligan"
    Then I cannot delete today appointment