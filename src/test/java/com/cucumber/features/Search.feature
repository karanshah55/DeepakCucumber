Feature: Search Dress and find all the elements related to it

@SearchDressFunctionality
Scenario Outline: Search Dress in the search bar
Given User enters username as "<username>"
	And User enters password as "<password>"
	And User will do login
	And User will enter "Dress" keyword into the global search text box and click find
	And User browse through all the shown result and collects the data
	Then User will check whether all the result has Dress keyword
	Examples:
    | username  						| password  |
    | karanshah55@gmail.com | Karan123  |