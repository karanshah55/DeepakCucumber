/*
 *
 */
package com.utility.selenium;

import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties extends Properties
{

	/** The Object of ApplicationProperties class 'props'. */
	private static ApplicationProperties props = null;

	/**
	 * Instantiates a new application properties as singlutorn class.
	 * @author GS-1629
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ApplicationProperties() throws IOException
	{
		load(getClass().getResourceAsStream("/configuration/configuration.properties"));
	}

	/**
	 * Gets the single instance of ApplicationProperties.
	 * @author GS-1629
	 * @return single instance of ApplicationProperties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static synchronized ApplicationProperties getInstance() throws IOException
	{
		if (props == null)
		{
			props = new ApplicationProperties();
		}
		return props;
	}
}
