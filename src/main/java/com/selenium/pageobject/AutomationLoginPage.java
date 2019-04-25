package com.selenium.pageobject;

import java.nio.charset.Charset;
import java.util.Random;

import org.openqa.selenium.By;

import com.utility.selenium.BasePageObject;

public class AutomationLoginPage extends BasePageObject{

	//private static final By userName=By.xpath("//input[@id='email']");
	private static final By userName=By.xpath("//input[@name='username']");
	//private static final By password=By.xpath("//input[@id='passwd']");
	private static final By password=By.xpath("//input[@name='password']");
	//private static final By loginButton=By.xpath("//button[@id='SubmitLogin']//span[1]");
	private static final By loginButton=By.xpath("//input[@value='Test Login']");
	private static final By signInButton=By.xpath("//a[@title='Log in to your customer account']");
	
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
		timeInterval(2);
	}
	

	public double getRandomNumber1(){
    double x = Math.random();
    return x;
	}
	
	
	public void givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
	    byte[] array = new byte[7]; // length is bounded by 7
	    new Random().nextBytes(array);
	    String generatedString = new String(array, Charset.forName("UTF-8"));
	 
	    System.out.println(generatedString);
	}
	
	 private static final String CHAR_LIST = 
		        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		    private static final int RANDOM_STRING_LENGTH = 10;
		     
		    
		    public String generateRandomString(){
		         
		        StringBuffer randStr = new StringBuffer();
		        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
		            int number = getRandomNumber();
		            char ch = CHAR_LIST.charAt(number);
		            randStr.append(ch);
		        }
		        return randStr.toString();
		    }
		     
		    public int getRandomNumber() {
		        int randomInt = 0;
		        Random randomGenerator = new Random();
		        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		        if (randomInt - 1 == -1) {
		            return randomInt;
		        } else {
		            return randomInt - 1;
		        }
		    }

	
	
}
