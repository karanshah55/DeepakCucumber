@loginToToolsQA
Feature: Log in to ebay


@loginScenario
Scenario Outline: Log in Scenario to toolsQA
Given User will provide username as "<username>"
	And User will provide password as "<password>"
	Then User will click Login
	
Examples:
    | username  						| password  |
    | karanshah55						| Kpower@123|  

