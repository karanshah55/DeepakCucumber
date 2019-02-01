/*
 *
 */
package com.utility.selenium;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public class BasePageObject<T>
{
	public static int Seconds = 30;
	private static Logger logger;
	protected static WebDriver driver;
	protected String actionTime;
	private Actions build;
	private JavascriptExecutor scriptExecutor;
	private int longPollingTime=20;
	private int pollingFrequencyTime=500;
	
	
	//instance block - just like default constructor but executes before constructor
	{
		driver = BaseTestScript.driver;
		PropertyConfigurator.configure("configuration/log4j.properties");
		logger = Logger.getLogger(BasePageObject.class);
		build = new Actions(driver);
		scriptExecutor = (JavascriptExecutor) driver;
	}

	
	public void mouseOver(By by){
		Actions action = new Actions(driver);
		WebElement we = driver.findElement(by);
		action.moveToElement(we).build().perform();
	}
	
	public void click(By by) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + element.getLocation().y + ")");
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to click Element on window : "+by+" On Page : " + e.getMessage());
		}

	}
		
	public boolean isElementPresent(By by) {
	    try {
	        driver.findElement(by);
	        return true;
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}
	
	public void clear(By by) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			element.clear();
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to Clear Element on window : "+by+" On Page : " + e.getMessage());
		}

	}

	
	public void sendKeys(By by, String keysToSend) {
	
			WebElement element= getHighlightElement(by);
			//build.moveToElement(element).build().perform();
			element.sendKeys(keysToSend);
		
		/*catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to SendKeys Element on window : "+by+" On Page : " + e.getMessage());
		}
*/
	}

	public void aactiveElement(){
		driver.switchTo().activeElement();
	}
	
	public void sendKeysWait(By by, String keysToSend, boolean cleanElement) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			if (cleanElement)
			{
				clear(by);
			}
			for (int i = 0; i < keysToSend.length(); i++)
			{
				timeInterval();
				driver.findElement(by).sendKeys(String.valueOf(keysToSend.charAt(i)));
			}
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to SendKeysWait Element on window : "+by+" On Page : " + e.getMessage());
		}

	}

	
	public String getText(By by) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			return element.getText();
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to GetText Element on window : "+by+" On Page : " + e.getMessage());
		}
		return null;
	}


	
	public String getAttribute(By by, String attribute) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			return element.getAttribute(attribute);
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to Get Attribute Element on window : "+by+" On Page : " + e.getMessage());
		}
		return null;
	}

	
	public boolean isSelected(By by) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			return element.isSelected();
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to Select Option from Element on window : "+by+" On Page : " + e.getMessage());
		}
		return false;
	}

	
	public boolean isEnabled(By by) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			return element.isEnabled();
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to verify Element is Enalble or not on window : "+by+" On Page : " + e.getMessage());
		}
		return false;
	}

	
	public boolean isDisplayed(By by) {
		try
		{
			WebElement element= getHighlightElement(by);
			build.moveToElement(element).build().perform();
			return element.isDisplayed();
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to verify Element is display or not on window : "+by+" On Page : " + e.getMessage());
		}
		return false;
	}

	
	public boolean isClickable(By by) {
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(by));
			return true;
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to verify Element is Clickable or not on window : "+by+" On Page : " + e.getMessage());
		}
		return false;
	}

	
	public int getNumberOfElements(By by)
	{
		return driver.findElements(by).size();
	}
	
	
	public void dragAndDrop(By source, By target) {
		WebElement sourceElement= getHighlightElement(source);
		WebElement targetElement= getHighlightElement(target);

		try
		{
			build.moveToElement(sourceElement).build().perform();
			build.dragAndDrop(sourceElement, targetElement).build().perform();
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
			Assert.assertTrue(false, "Fail to drag and drop from : " + source + " to : " + target + " on page: " + e.getMessage());
		}
	}

	
	public String getPopupMsgAndAccept() {
		String msg="";
		try
		{
			msg=driver.switchTo().alert().getText();
			driver.switchTo().alert().accept();
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to Handle Alert on window : " + e.getMessage());
		}
		return msg;
	}

	
	public void scroll(int verticalScroll) {
		scriptExecutor.executeScript("window.scrollBy(0," + verticalScroll + ")");
	}

	
	public void scrollToBottom()
	{
		timeInterval();
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		timeInterval();
	}

	
	public void scrollToTop()
	{
		Boolean vertscrollStatus = (Boolean) scriptExecutor.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
		if (vertscrollStatus)
		{
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.HOME);
			timeInterval();
		}
		else
		{
			scriptExecutor.executeScript("scrollBy(0, -1000)");
		}
	}

	
	public void scrollToElement(By by) {
		try
		{
			WebElement element = getHighlightElement(by);
			scriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
			timeInterval();
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to ScrollToElement on window : "+by+" On Page : " + e.getMessage());
		}
	}

	
	public WebElement getHighlightElement(By by) {
		WebElement element=null;
		//try
		//{
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(longPollingTime, TimeUnit.SECONDS)
					.pollingEvery(pollingFrequencyTime, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			element = wait.until(new Function<WebDriver, WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(by);
				}
			});
			// Element is now available for further use.
			((JavascriptExecutor) driver)
			.executeScript("arguments[0].style.border='2px solid red'", element);
		//}
		/*catch (Exception e)
		{
			logger.info(e.getMessage());
		}*/
		return element;
	}

	
	public void timeInterval() {
		timeInterval(1);		
	}
	
	public void timeInterval(int second) {
		try {
			Thread.sleep(second*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	
	public void waitForParticularElement(By by, int second) {
		int count=1;
		while(count<=longPollingTime)
		{
			try {
				driver.findElement(by).isDisplayed();
				break;
			} catch (Exception e) {
				timeInterval();
				count++;
			}
		}
	}

	
	public String getCssValue(By by, String propertyName) {
		WebElement element = getHighlightElement(by);
		try
		{
			return element.getCssValue(propertyName).trim();
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to get CSS value from : " + by + " on page : " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
		return null;
	}

	
	public static void getScreenshotAs(String screenName) throws WebDriverException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(BaseTestScript.REPORTLOCATION+"/"+screenName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void selectOptionFromDropDown(By by, String option) {
		WebElement element = getHighlightElement(by);
		build.moveToElement(element).build().perform();
		Select select_list = null;
		try
		{
			timeInterval();
			select_list = new Select(element);
			select_list.selectByVisibleText(option);
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to find drop down box to select option : " + by + " on page : " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
	}

	
	public String getValueFromDropDown(By by) {
		WebElement element = getHighlightElement(by);
		build.moveToElement(element).build().perform();
		Select select_list = null;
		try
		{
			timeInterval();
			select_list = new Select(element);
			return select_list.getFirstSelectedOption().getText().trim();
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to find drop down box to select option : " + by + " on page : " + e.getMessage());
			logger.info("Fail to find drop down box to select option : " + by + " on page : " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
		return null;
	}

	public List<String> getValuesFromDropDown(By by) {
		WebElement element = getHighlightElement(by);
		build.moveToElement(element).build().perform();
		List<String> options = new ArrayList<>();
		List<WebElement> options_Elmnt = null;
		try
		{
			timeInterval();
			Select select_otions = new Select(element);
			options_Elmnt = select_otions.getOptions();
			for (WebElement ele : options_Elmnt)
			{
				options.add(ele.getText().trim());
			}
		}
		catch (NoSuchElementException e)
		{
			Assert.assertTrue(false, "Fail to fetch value from drop down box on page : " + e.getMessage());
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
		return options;
	}

}
