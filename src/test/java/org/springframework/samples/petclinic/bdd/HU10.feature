Feature: Cannot make appointment without paying 
   I cannot make an appointment without paying previous ones
  
  Scenario Outline: I cannot make an appointment
    Given I am logged in the system as <name> with password <password>
    When I add a new appointment for pet <pet> with date <date>
    Then I cannot create an appointment without paying previous ones
	
	Examples: 
      | name     | password |      pet    |        date        |
      | "owner6" | "0wn3r"  |  "Samantha" | "2020/08/05 20:00" |