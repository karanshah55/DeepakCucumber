/*
 * This class is used for set/get HTML report path
 * set/get extentXServerUrl and set/get projectName
 * For generated HTML report.
 */
package com.cucumber.listener;

import java.io.File;

/**
 * An enum which holds the properties to be set for extent reporter.
 * This class is used for set/get HTML report path
 * set/get extentXServerUrl and set/get projectName
 * For generated HTML report.
 */
public enum ExtentProperties {

	/** The instance. */
	INSTANCE;

	/** The report path. */
	private String reportPath;

	/** The extent X server url. */
	private String extentXServerUrl;

	/** The project name. */
	private String projectName;

	/**
	 * Instantiates a new extent properties.
	 * This method will construct new path of HTML report,
	 * folder path like: ../output/Run_systeTime/report.html
	 * @author GS-1629
	 */
	ExtentProperties() {
		this.reportPath = "output" + File.separator + "Run_" + System.currentTimeMillis() + File.separator
				+ "report.html";
		this.projectName = "default";
	}

	/**
	 * Gets the report path.
	 * @author GS-1629
	 * @return The report path
	 */
	public String getReportPath() {
		return reportPath;
	}

	/**
	 * Sets the report path.
	 * @author GS-1629
	 * @param reportPath The report path value
	 */
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	/**
	 * Gets the ExtentX server URL.
	 * @author GS-1629
	 * @return The ExtentX server URL
	 */
	public String getExtentXServerUrl() {
		return extentXServerUrl;
	}

	/**
	 * Sets the ExtentX server URL.
	 * @author GS-1629
	 * @param extentXServerUrl The ExtentX server URL
	 */
	void setExtentXServerUrl(String extentXServerUrl) {
		this.extentXServerUrl = extentXServerUrl;
	}

	/**
	 * Gets the project name.
	 * @author GS-1629
	 * @return The project name
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Gets the project name.
	 * @author GS-1629
	 * @param projectName The project name
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
