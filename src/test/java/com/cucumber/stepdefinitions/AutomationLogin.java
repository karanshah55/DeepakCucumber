package com.cucumber.stepdefinitions;

import java.util.List;

import com.selenium.pageobject.AutomationLoginPage;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class AutomationLogin {

	AutomationLoginPage login = new AutomationLoginPage();
	
	@Given("^User enters username and password$")
	public void user_enters_username_and_password(DataTable arg1) throws Throwable {
		List<List<String>> ls = arg1.raw();
		String userName = ls.get(0).get(0);
		String passWord = ls.get(0).get(1);
		
		login.enterUsername(userName);	
		login.enterPassword(passWord);
		login.doLogin();
	}
	
	@Given("^User enters username as \"([^\"]*)\"$")
	public void user_enters_username_as(String userName) throws Throwable {
		login.enterUsername(userName);	
		String abc = login.generateRandomString();
		System.out.println(abc);
		int str=login.getRandomNumber();
		System.out.println(str);
		
	}

	@Given("^User enters password as \"([^\"]*)\"$")
	public void user_enters_password_as(String passWord) throws Throwable {
		 login.enterPassword(passWord);	
	}

	@Then("^User will do login$")
	public void user_will_do_login() throws Throwable {
	    login.doLogin();
	}
	
}
