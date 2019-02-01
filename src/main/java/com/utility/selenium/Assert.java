/*
 *
 */
package com.utility.selenium;

import java.io.IOException;

import com.cucumber.listener.Reporter;

public class Assert
{

	public static void assertTrue(boolean expectedResult)
	{
		if (!expectedResult)
		{
			String timeStamp=BaseTestScript.dateAndSystemTime();
			timeStamp = "AssertTrueFail_"+timeStamp.replace(":", "_")+".png";
			BasePageObject.getScreenshotAs(timeStamp);
			try {
				Reporter.addScreenCaptureFromPath(timeStamp, "AssertionFail");
			} catch (IOException e) {
				e.printStackTrace();
			}
			org.testng.Assert.assertTrue(expectedResult);
		}

	}

	public static void assertTrue(boolean expectedResult, String message)
	{
		if (!expectedResult)
		{
			String timeStamp=BaseTestScript.dateAndSystemTime();
			timeStamp = "AssertTrueFail_"+timeStamp.replace(":", "_")+".png";
			BasePageObject.getScreenshotAs(timeStamp);
			try {
				Reporter.addScreenCaptureFromPath(timeStamp, "AssertionFail");
			} catch (IOException e) {
				e.printStackTrace();
			}
			org.testng.Assert.assertTrue(expectedResult, message);
		}
	}

	public static void assertFalse(boolean expectedResult)
	{
		if (expectedResult)
		{
			String timeStamp=BaseTestScript.dateAndSystemTime();
			timeStamp = "AssertFalseFail_"+timeStamp.replace(":", "_")+".png";
			BasePageObject.getScreenshotAs(timeStamp);
			try {
				Reporter.addScreenCaptureFromPath(timeStamp, "AssertionFail");
			} catch (IOException e) {
				e.printStackTrace();
			}
			org.testng.Assert.assertFalse(expectedResult);
		}
	}

	public static void assertEquals(boolean actual, boolean expected)
	{
		if (actual != expected)
		{
			String timeStamp=BaseTestScript.dateAndSystemTime();
			timeStamp = "AssertEqualFail_"+timeStamp.replace(":", "_")+".png";
			BasePageObject.getScreenshotAs(timeStamp);
			try {
				Reporter.addScreenCaptureFromPath(timeStamp, "AssertionFail");
			} catch (IOException e) {
				e.printStackTrace();
			}
			org.testng.Assert.assertEquals(actual, expected);
		}
	}

	public static void assertEquals(String Value, String Value1, String message)
	{
		org.testng.Assert.assertEquals(Value, Value1, message);
	}
}
