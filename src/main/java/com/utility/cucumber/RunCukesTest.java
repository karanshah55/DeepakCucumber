/*
 *
 */
package com.utility.cucumber;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.cucumber.listener.ExtentProperties;
import com.cucumber.listener.Reporter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * This class will load cucumber setting as per given and execute before and after annotation
 * and also load and destroy cucumber objects.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/java/com/cucumber/features/"},
		glue = {"com.cucumber.stepdefinitions"},
		plugin = {"com.cucumber.listener.ExtentCucumberFormatter:"}
		)
public class RunCukesTest {

	/**
	 * Initialized ExetentReport object and set it's property such as
	 * Project Name, Report Generation Location, etc.
	 * @author GS-1629
	 */
	@BeforeClass
	public static void setup() {
		ExtentProperties extentProperties = ExtentProperties.INSTANCE;
		extentProperties.setProjectName("Automation EDB");
		extentProperties.setReportPath("output/report.html");
	}

	/**
	 * It tear down ExetentReport object and load report generation settings from XML.
	 * and set systemInfo such as user, OS, RunnerOutput name.
	 * @author GS-1629
	 */
	@AfterClass
	public static void teardown() {
		Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
		Reporter.setSystemInfo("user", System.getProperty("user.name"));
		Reporter.setSystemInfo("os", System.getProperty("os.name").toUpperCase());
		Reporter.setTestRunnerOutput("EBD Automation Reports - CME");
	}

}
