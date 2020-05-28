Feature: Cannot answer an announcement with no vacinnated pets
		I cannot create an answer to an appointment if my pets re not vacinnated

  Scenario: I cannot create an answer to an announcement
  	Given I am logged in the system as "owner6" with password "0wn3r"
  	When I try to add a new answer to announcement "Anuncio1"
  	Then I cannot create an answer without vaccinating my pets