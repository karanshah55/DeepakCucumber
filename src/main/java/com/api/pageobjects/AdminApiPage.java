/*
 * This page is contain all API calls
 * related to AdminApi.
 */
package com.api.pageobjects;

import com.utility.api.BaseApiClass;

/**
 * The Class AdminApiPage.
 * This page is contain all API calls
 * related to AdminApi.
 */
public class AdminApiPage extends BaseApiClass{

	/** The admin api. */
	private static AdminApiPage adminApi = null;

	/**
	 * Instantiates a new admin api page.
	 *
	 * @author GS-1629
	 * Instantiates a new admin api page.
	 */
	private AdminApiPage()
	{
		// It is for restricting creating object of AdminAdmin from outside of class
	}

	/**
	 * Gets the single instance of AdminApiPage.
	 *@author GS-1629
	 * @return single instance of AdminApiPage
	 */
	public static synchronized AdminApiPage getInstance()
	{
		if (adminApi == null)
		{
			adminApi = new AdminApiPage();
		}
		return adminApi;
	}
}
