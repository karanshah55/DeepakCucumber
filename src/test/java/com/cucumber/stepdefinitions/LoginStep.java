/*
 * This is Login page step definition.
 * This page will contains all steps which is related to login functionality
 * step definition is bind with cucumber steps.
 */
package com.cucumber.stepdefinitions;


import com.cucumber.listener.Reporter;
import com.utility.selenium.BaseTestScript;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * The Class LoginStep.
 * This is Login page step definition.
 * This page will contains all steps which is related to login functionality
 * step definition is bind with cucumber steps.
 */
public class LoginStep extends BaseTestScript
{

	//	/** The login. */
	//	LoginPage login;
	//
	//	/**
	//	 * Cucumber before.
	//	 * @author GS-1629
	//	 * @param scenario the scenario
	//	 * @throws IOException Signals that an I/O exception has occurred.
	//	 * @throws InterruptedException the interrupted exception
	//	 */
	//	@Before
	//	public void cucumberBefore(Scenario scenario) throws IOException, InterruptedException
	//	{
	//		Reporter.assignAuthor("Deepak Rathod");
	//		Reporter.addScenarioLog(scenario.getName());
	//		beforeTest();
	//		testCaseStartTime();
	//		beforeMethod();
	//	}
	//
	//	/**
	//	 * After cucumber.
	//	 * @author GS-1629
	//	 * @throws IOException Signals that an I/O exception has occurred.
	//	 */
	//	@After
	//	public void afterCucumber() throws IOException
	//	{
	//		testCaseEndTime();
	//		afterTest();
	//	}
	//
	//	/**
	//	 * The user enters the availity app URL.
	//	 * @author GS-1629
	//	 * @throws Throwable the throwable
	//	 */
	//	@Given("^The user enters the Availity app URL$")
	//	public void the_user_enters_the_Availity_app_URL() throws Throwable {
	//		System.out.println("This is step defination");
	//	}
	//
	//	/**
	//	 * Do login.
	//	 * @author GS-1629
	//	 * @throws Throwable the throwable
	//	 */
	//	@When("^The user enters valid email address & password$")
	//	public void doLogin() throws Throwable {
	//		login = new LoginPage();
	//		Reporter.addStepLog("User login with "+BaseTestScript.USERNAME+"as username and "+ BaseTestScript.PASSWORD+" as password");
	//		login.doLogin(BaseTestScript.USERNAME, BaseTestScript.PASSWORD, false);
	//		Reporter.addStepLog("click on login button");
	//		login.clickOnLoginBtn();
	//	}
	//
	//	/**
	//	 * The user enters valid email address password 1.
	//	 * @author GS-1629
	//	 * @throws Throwable the throwable
	//	 */
	//	@When("^the user selects New york State from Drop down$")
	//	public void the_user_enters_valid_email_address_password1() throws Throwable {
	//		// Write code here that turns the phrase above into concrete actions
	//		//throw new PendingException();
	//	}
	
	@When("^Testing scenario$")
	public void testing_scenario() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    System.out.println("Testing Scneario in When");
	    Reporter.addScreenCaptureFromPath("C:\\Users\\TKS\\Desktop\\UTR.jpg");
	}

	@Then("^Match expected$")
	public void match_expected() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("Testing Scneario in Then");
	}
	
	@Given("^User login with username \"([^\"]*)\"$")
	public void user_login_with_username(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    System.out.println(arg1);
	}

	@When("^User login with password \"([^\"]*)\"$")
	public void user_login_with_password(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    System.out.println(arg1);
	}

}
