@ScheduleReport
Feature: Test

  @TC_SR_001
  Scenario: Test scenario
  	When Testing scenario
  	Then Match expected
  	
  @ScenarioOutlineTest	
  Scenario Outline: Scenario Outline
  Given User login with username "<username>"
  When User login with password "<password>"
	Examples:
	| username | password|
	|abc.com	 | abc |
	|xyz.come	 | xyz |