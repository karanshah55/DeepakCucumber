package com.selenium.pageobject;

import java.nio.charset.Charset;
import java.util.Random;

import org.openqa.selenium.By;

import com.utility.selenium.BasePageObject;

public class AstoLoginPage extends BasePageObject{

	
	private static final By userName=By.xpath("//input[@name='username']");
	private static final By password=By.xpath("//input[@name='password']");
	private static final By loginButton=By.xpath("//input[@value='Test Login']");
	private static final By failedLogin=By.xpath("//b[contains(text(),'**Failed Login**')]");
	private static final By successfulLogin=By.xpath("//b[contains(text(),'**Successful Login**')]");
	
	public void enterUsername(String userName) throws Exception{
		//click(this.userName);
		sendKeys(this.userName, userName);
	}
	
	public void enterPassword(String password){
		//click(this.password);
		sendKeys(this.password, password);
	}

	public void doLogin(){
		click(loginButton);
		timeInterval(3);
	}
	
	public boolean isFailedLoginDisplayed(){
		return isDisplayed(failedLogin);
	}
	
	public boolean isSuccessfulLoginDisplayed(){
		return isDisplayed(successfulLogin);
	}

	
	
}
