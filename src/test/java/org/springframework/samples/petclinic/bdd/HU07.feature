Feature: Cannot answer an announcement if negative history
		I cannot create an answer to an announcement if my history is negative

  Scenario: I cannot create an answer to an announcement
    Given I am logged in the system as "owner4" with password "0wn3r"
    When I try to add a new answer to announcement "Anuncio2" with negative history
    Then I cannot create an answer with a negative history