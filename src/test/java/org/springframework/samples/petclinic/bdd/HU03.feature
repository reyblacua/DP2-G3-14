Feature: Answer to an Announcement
		I want to answer to an announcement
		
		Scenario Outline: I answer to an announcement
			Given I am logged in the system as <name> with password <password>
			When I add an answer to announcement <nameAnnoucement> with answer description <description>
			Then I am redirected to the announcement <nameAnnoucement>
			
			Examples: 
      | name      | password |   description       |  nameAnnoucement  |
      | "owner11" | "0wn3r"  |  "This is a test"   |  "Anuncio1"       |
      | "owner12" | "0wn3r"  | "This is a test 2"  |  "Anuncio2"       | 
      
    Scenario: I can not answer to an announcement
    	Given I am not logged in the system
    	When I try to add an answer to announcement "Anuncio1"
    	Then I can not create an answer
    	
    Scenario: I can not answer twice the same  announcement
    	Given I am logged in the system as "owner1" with password "0wn3r"
    	When I try to add answer to announcement "Anuncio2" again
    	Then I am redirected to the error page
    	
    Scenario: I can not answer mi own announcement
    	Given I am logged in the system as "owner1" with password "0wn3r"
    	When I try to add answer to my own announcement "Anuncio1"
    	Then I can not create an answer to my own announcement
    