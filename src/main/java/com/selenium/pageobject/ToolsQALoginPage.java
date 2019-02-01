package com.selenium.pageobject;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.utility.selenium.BasePageObject;

public class ToolsQALoginPage extends BasePageObject{
	

	private static final By userName=By.id("log");
	private static final By password=By.id("pwd");
	private static final By myAccount=By.xpath("//a[@title='My Account']");
	private static final By logInButton=By.id("login");
	
	public void enterUsername(String userName) throws Exception{
		sendKeys(this.userName, userName);
	}
	
	public void enterPassword(String password){
		sendKeys(this.password, password);
	}

	public void clickMyAccount(){
		click(myAccount);
	}
	
	public void clicklogInButton(){
		click(logInButton);
		
	}
}
