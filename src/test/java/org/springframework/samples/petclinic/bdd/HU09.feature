Feature: Appointment for hairdresser 
   I want to make an appointment for an hairdresser
  
  Scenario Outline: I make an appointment
    Given I am logged in the system as <name> with password <password>
    When I add a new appointment for pet <pet> with date <date>
    Then the appointment appears in my appointments
	
	Examples: 
      | name     | password |   pet    |        date        |
      | "owner1" | "0wn3r"  |  "Leo"   | "2020/08/04 20:00" |
      | "owner2" | "0wn3r"  | "Basil"  | "2020/08/03 20:00" |
	
	Scenario: I cannot make an appointment
    Given I am logged in the system as "owner12" with password "0wn3r"
    When I have no pets
    Then I can not create an appointment