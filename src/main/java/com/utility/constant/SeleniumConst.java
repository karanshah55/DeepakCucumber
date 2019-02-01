/*
 *
 */
package com.utility.constant;

/**
 * The Class CmeSelenium.
 */
public class SeleniumConst {

	/**
	 * Instantiates a new CME Selenium.
	 */
	private SeleniumConst()
	{
		// Do Nothing
	}

	public enum StatusIcon
	{
		Requested,
		Ready,
		Done,
		No_Recoreds,
		Failed
	}

	public enum ColumnName
	{
		User_ID,
		Category,
		Report,
		Criteria,
		Time,
		Description,
		Frequancy,
		Runtime
	}

	public enum Sorting
	{
		Ascending,
		Descending
	}
	//---------------------------- APPLICATION TEXTS -----------------------------
	public static final String REQUESTED_REPORTS="Requested Reports";
	public static final String SCHEDULE_REPORTS="Schedule Reports";
	public static final String ENABLE="ENABLE";
	public static final String DISABLE="DISABLE";

	// --------------------------- DOWNLOAD PATH  --------------------------------
	/** The Constant SYSTEM_USER_HOME. which indicate user directory HOME path*/
	public static final String SYSTEM_USER_HOME = System.getProperty("user.home");

	/** The filepath which contains path of resource document for read, write data. */
	public static String filepath = "/src/test/resources/documents/";

	/** The Constant FILE_PATH_DOWNLOADED hold path of where document will download. */
	public static final String FILE_PATH_DOWNLOADED = SYSTEM_USER_HOME + "\\Downloads\\";

}
