Feature: Owner can add a Exotic/Dangerous pet
   I dont want to add a pet if a owner has not a Exotic/Dangerous permission
  
  Scenario: I add a Exotic/Dangerous pet with the permission
    Given I am logged in the system as "owner5" with password "0wn3r"
    When I add a new pet with name "Taiga" and petType "cat" with the dangerous&exotic permission
    Then the pet with name "Taiga" and petType "cat" appears in my owners profile with the dangerous&exotic permission

	Scenario: I cannot add a Exotic/Dangerous pet without the permission
    Given I am logged in the system as "owner1" with password "0wn3r"
    When I add a new pet with name "Milu" and petType "cat" without the dangerous&exotic permission
    Then the pet is not added in my owners profile without the dangerous&exotic permission
	
    