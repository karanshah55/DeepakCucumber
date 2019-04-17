/*
 *
 */
package com.utility.cucumber;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.cucumber.listener.ExtentProperties;
import com.cucumber.listener.Reporter;
import com.utility.file.Downloaded_Verifier;
import com.utility.selenium.BaseTestScript;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

/**
 * The Class TestNGCukesRunner.
 * This class will load cucumber setting as per given and execute before and after annotation
 * and also load and destroy cucumber objects.
 */
@CucumberOptions(
		features = {"src/test/java/com/cucumber/features/"},
		tags = {"@AstoLoginscenarios"},
		glue = {"com.cucumber.stepdefinitions"},
		plugin = {"com.cucumber.listener.ExtentCucumberFormatter:","json:output/cucumber.json"}
		)
public class TestNGCukesRunner extends AbstractTestNGCucumberTests {

	/**
	 * Initialized ExetentReport object and set it's property such as
	 * Project Name, Report Generation Location, etc.
	 * @author GS-1629
	 * @throws IOException
	 */
	@BeforeSuite
	public  static void setup() throws IOException{
		ExtentProperties extentProperties = ExtentProperties.INSTANCE;
		extentProperties.setProjectName("Automation Reporting)");
		if(StringUtils.isEmpty(BaseTestScript.REPORTLOCATION)) {
			BaseTestScript.REPORTLOCATION=System.getProperty("user.dir")+"/output/LastExecution";
		}
		extentProperties.setReportPath(BaseTestScript.REPORTLOCATION+"/report.html");
		Downloaded_Verifier download = new Downloaded_Verifier();
		download.deleteAllFilesFromDirectory(BaseTestScript.REPORTLOCATION);
	}

	@BeforeTest
	public void beforeTest() throws IOException, InterruptedException
	{
		BaseTestScript script = new BaseTestScript();
		script.beforeTest();
		script.testCaseStartTime();
		script.beforeMethod();
	}

	@AfterTest
	public void afterTest() throws IOException
	{
		BaseTestScript script = new BaseTestScript();
		script.testCaseEndTime();
		script.afterTest();
	}
	/**
	 * It tear down ExetentReport object and load report generation settings from XML.
	 * and set systemInfo such as user, OS, RunnerOutput name.
	 * @author GS-1629
	 * @throws IOException
	 */
	@AfterSuite
	public static void tearDown() throws IOException
	{
		Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
		Reporter.setSystemInfo("user", System.getProperty("user.name"));
		Reporter.setSystemInfo("os", System.getProperty("os.name").toUpperCase());
		Reporter.setTestRunnerOutput("Automation Reports");

//		String timestamp = BaseTestScript.dateAndSystemTime().replace(":", "_");
//		EmailHelper email = new EmailHelper();
//		Downloaded_Verifier download = new Downloaded_Verifier();
//		try {
//			download.ZipCreation();
//			download.moveFileFromSourceToDestination("Automation-Report.zip", BaseTestScript.REPORTLOCATION, System.getProperty("user.dir")+"/output/ReportHistory/"+timestamp );
//			if(BaseTestScript.MAILSEND.equalsIgnoreCase("yes")) {
//				email.sendMailViaGmail();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		System.out.println("verify report generation");
	}
}
