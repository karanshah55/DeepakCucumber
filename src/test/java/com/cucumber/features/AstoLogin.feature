@AstoLoginscenarios
Feature: Asto Login Scenarios

  
Scenario: Login Scenarios for Asto
    Given User enters right username and right password on demosite and it accepts it
      | karan123 | karan123 |
    Given User enters right username and wrong password on demosite and it fails
    	| karan | wrongpasswd |
    Given User enters wrong username and right password on demosite and it fails
    	| wrongUserName | karan |
    Given User enters wrong username and wrong password on demosite and it fails
    	| karan | wrongpasswd |