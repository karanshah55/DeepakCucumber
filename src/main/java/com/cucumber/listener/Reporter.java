/*
 *
 */
package com.cucumber.listener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

/**
 * This class is used for add steps, scenario, feature logs into HTML report
 * Different methods imposed in this class for logging steps into report.
 * screen shot also can be add into HTML report by this class.
 */
public class Reporter {

	/** The system info key map. */
	private static Map<String, Boolean> systemInfoKeyMap = new HashMap<>();

	/**
	 * Instantiates a new reporter.
	 * @author GS-1629
	 */
	private Reporter() {
		// Defeat instantiation
	}

	/**
	 * Gets the {@link ExtentHtmlReporter} instance created through listener.
	 * @author GS-1629
	 * @return The {@link ExtentHtmlReporter} instance
	 */
	public static ExtentHtmlReporter getExtentHtmlReport() {
		return ExtentCucumberFormatter.getExtentHtmlReport();
	}

	/**
	 * Gets the {@link ExtentReports} instance created through the listener.
	 * @author GS-1629
	 * @return The {@link ExtentReports} instance
	 */
	public static ExtentReports getExtentReport() {
		return ExtentCucumberFormatter.getExtentReport();
	}

	/**
	 * Loads the XML config file.
	 * @author GS-1629
	 * @param xmlPath
	 *            The xml path in string
	 */
	public static void loadXMLConfig(String xmlPath) {
		getExtentHtmlReport().loadXMLConfig(xmlPath);
	}

	/**
	 * Loads the XML config file.
	 * @author GS-1629
	 * @param file
	 *            The file path of the XML
	 */
	public static void loadXMLConfig(File file) {
		getExtentHtmlReport().loadXMLConfig(file);
	}

	/**
	 * Adds an info message to the current step.
	 * @author GS-1629
	 * @param message
	 *            The message to be logged to the current step
	 */
	public static void addStepLog(String message) {
		getCurrentStep().info(message);
	}

	/**
	 * Adds an info message to the current scenario.
	 * @author GS-1629
	 * @param message
	 *            The message to be logged to the current scenario
	 */
	public static void addScenarioLog(String message) {
		getCurrentScenario().info(message);
	}

	/**
	 * Adds the screenshot from the given path to the current step.
	 * @author GS-1629
	 * @param imagePath
	 *            The image path
	 * @throws IOException
	 *             Exception if imagePath is erroneous
	 */
	public static void addScreenCaptureFromPath(String imagePath) throws IOException {
		getCurrentStep().addScreenCaptureFromPath(imagePath);
	}

	/**
	 * Adds the screenshot from the given path with the given title to the
	 * current step.
	 * @author GS-1629
	 * @param imagePath
	 *            The image path
	 * @param title
	 *            The title for the image
	 * @throws IOException
	 *             Exception if imagePath is erroneous
	 */
	public static void addScreenCaptureFromPath(String imagePath, String title) throws IOException {
		getCurrentStep().addScreenCaptureFromPath(imagePath, title);
	}

	/**
	 * Adds the screen cast from the given path to the current step.
	 * @author GS-1629
	 * @param screenCastPath
	 *            The screen cast path
	 * @throws IOException
	 *             Exception if imagePath is erroneous
	 */
	public static void addScreenCast(String screenCastPath) throws IOException {
		getCurrentStep().addScreencastFromPath(screenCastPath);
	}

	/**
	 * Sets the system information with the given key value pair.
	 * @author GS-1629
	 * @param key
	 *            The name of the key
	 * @param value
	 *            The value of the given key
	 */
	public static void setSystemInfo(String key, String value) {
		if (systemInfoKeyMap.isEmpty() || !systemInfoKeyMap.containsKey(key)) {
			systemInfoKeyMap.put(key, false);
		}
		if (systemInfoKeyMap.get(key)) {
			return;
		}
		getExtentReport().setSystemInfo(key, value);
		systemInfoKeyMap.put(key, true);
	}

	/**
	 * Sets the test runner output with the given list of strings.
	 * @author GS-1629
	 * @param log
	 *            The list of string messages
	 */
	public static void setTestRunnerOutput(List<String> log) {
		getExtentReport().setTestRunnerOutput(log);
	}

	/**
	 * Sets the test runner output with the given string.
	 * @author GS-1629
	 * @param outputMessage
	 *            The message to be shown in the test runner output screen
	 */
	public static void setTestRunnerOutput(String outputMessage) {
		getExtentReport().setTestRunnerOutput(outputMessage);
	}

	/**
	 * Sets the author name for the current scenario.
	 * @author GS-1629
	 * @param authorName
	 *            The author name of the current scenario
	 */
	public static void assignAuthor(String... authorName) {
		getCurrentScenario().assignAuthor(authorName);
	}

	/**
	 * Gets the current step.
	 * @author GS-1629
	 * @return the current step
	 */
	private static ExtentTest getCurrentStep() {
		return ExtentCucumberFormatter.stepTestThreadLocal.get();
	}

	/**
	 * Gets the current scenario.
	 * @author GS-1629
	 * @return the current scenario
	 */
	private static ExtentTest getCurrentScenario() {
		return ExtentCucumberFormatter.scenarioThreadLocal.get();
	}
}
