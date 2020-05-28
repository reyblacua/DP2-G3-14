Feature: Announcement List & Show
   I want to list and show announcements of pets
  
  Scenario: I can list announcements
    Given I am not logged in the system
    When I enter on announcements section
    Then the announcements are listed

