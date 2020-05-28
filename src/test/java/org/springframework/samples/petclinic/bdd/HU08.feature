Feature: Cannot answer an announcement if pet is not ready for adopting
		I cannot create an answer to an announcement if pet is not ready for adopting

  Scenario: I cannot create an answer to an announcement
    Given I am logged in the system as "owner5" with password "0wn3r"
    When I try to add a new answer to announcement "Anuncio3" with pet not ready for adopting
    Then I cannot create an answer with pet not ready for adopting

