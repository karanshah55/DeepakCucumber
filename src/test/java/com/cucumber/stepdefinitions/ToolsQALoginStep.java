package com.cucumber.stepdefinitions;


import com.selenium.pageobject.ToolsQALoginPage;
import com.utility.selenium.BaseTestScript;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class ToolsQALoginStep extends BaseTestScript{
	
	ToolsQALoginPage login = new ToolsQALoginPage();
	
	@Given("^User will provide username as \"([^\"]*)\"$")
	public void user_will_provide_username_as(String arg1) throws Throwable {
		login.clickMyAccount();
		login.enterUsername(arg1);	    
	}

	@Given("^User will provide password as \"([^\"]*)\"$")
	public void user_will_provide_password_as(String arg1) throws Throwable {
	    login.enterPassword(arg1);	    
	}

	@Then("^User will click Login$")
	public void user_will_click_Login() throws Throwable {
		login.clicklogInButton();
		login.timeInterval(8);
	}
}
