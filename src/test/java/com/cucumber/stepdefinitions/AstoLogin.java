package com.cucumber.stepdefinitions;

import gherkin.formatter.model.DataTableRow;

import java.util.List;

import org.testng.Assert;

import com.selenium.pageobject.AstoLoginPage;
import com.selenium.pageobject.AutomationLoginPage;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class AstoLogin {

	AstoLoginPage login = new AstoLoginPage();
	com.selenium.pageobject.AddToCart cart = new com.selenium.pageobject.AddToCart();
	List<DataTableRow> userNameAndPassword;
	
	@Given("^User enters right username and right password on demosite and it accepts it$")
	public void user_enters_right_username_and_right_password_on_demosite_and_it_accepts_it(DataTable userPasswd) throws Throwable {
		List<List<String>> ls = userPasswd.raw();
		String userName = ls.get(0).get(0);
		String passWord = ls.get(0).get(1);
		
		login.enterUsername(userName);	
		login.enterPassword(passWord);
		login.doLogin();
		Assert.assertEquals(login.isSuccessfulLoginDisplayed(), true); 
	}

	@Given("^User enters right username and wrong password on demosite and it fails$")
	public void user_enters_right_username_and_wrong_password_on_demosite_and_it_fails(DataTable userPasswd) throws Throwable {
		List<List<String>> ls = userPasswd.raw();
		String userName = ls.get(0).get(0);
		String passWord = ls.get(0).get(1);
		
		login.enterUsername(userName);	
		login.enterPassword(passWord);
		login.doLogin();
		Assert.assertEquals(login.isFailedLoginDisplayed(), true); 
	}

	@Given("^User enters wrong username and right password on demosite and it fails$")
	public void user_enters_wrong_username_and_right_password_on_demosite_and_it_fails(DataTable userPasswd) throws Throwable {
		List<List<String>> ls = userPasswd.raw();
		String userName = ls.get(0).get(0);
		String passWord = ls.get(0).get(1);
		
		login.enterUsername(userName);	
		login.enterPassword(passWord);
		login.doLogin();
		Assert.assertEquals(login.isFailedLoginDisplayed(), true); 
	}

	@Given("^User enters wrong username and wrong password on demosite and it fails$")
	public void user_enters_wrong_username_and_wrong_password_on_demosite_and_it_fails(DataTable userPasswd) throws Throwable {
		List<List<String>> ls = userPasswd.raw();
		String userName = ls.get(0).get(0);
		String passWord = ls.get(0).get(1);
		
		login.enterUsername(userName);	
		login.enterPassword(passWord);
		login.doLogin();
		Assert.assertEquals(login.isFailedLoginDisplayed(), true); 
	}

	
}
