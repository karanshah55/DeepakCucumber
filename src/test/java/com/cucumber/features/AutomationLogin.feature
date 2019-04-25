Feature: Log into Automation


@AutomationloginScenario
Scenario Outline: Log in scenario for Automation
Given User enters username as "<username>"
	And User enters password as "<password>"
	Then User will do login
Examples:
    | username  						| password  |
    | karanshah55@gmail.com | Karan123  |     
    
    