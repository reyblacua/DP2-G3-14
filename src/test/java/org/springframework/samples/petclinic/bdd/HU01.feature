Feature: Announcement creation
   I want to create an announcement of a pet
  
  Scenario Outline: I create an announcement
    Given I am logged in the system as <user> with password <password>
    When I add a new announcement for pet <pet> with announcement <name>, description <description> and petType <petType>
    Then the announcement appears in my announcements
	
	Examples: 
      | user     | password |   pet    | name                |  description     | petType |
      | "owner1" | "0wn3r"  |  "Leo"   | "anuncio ejemplo 1" | "this is a text" | "dog"   |

  Scenario: I cannot create an announcement
    Given I am not logged in the system
    When I list announcements
    Then the create announcements button does not appear