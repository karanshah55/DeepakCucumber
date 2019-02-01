/*
 *
 */
package com.selenium.pageobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.utility.constant.SeleniumConst;
import com.utility.constant.SeleniumConst.ColumnName;
import com.utility.constant.SeleniumConst.Sorting;
import com.utility.constant.SeleniumConst.StatusIcon;
import com.utility.selenium.BasePageObject;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestedReportPage.
 */
public class RequestedReportPage extends BasePageObject<T> {

	/**
	 * ==================================================================================
	 * Verification methods related to HEADERS (TOP) AREA from EDB reporting
	 * application
	 * ==================================================================================.
	 *
	 * @param searchString the search string
	 */

	public void gotoGlobleSearch(String searchString) {
		sendKeys(By.id(""), searchString);
	}

	/**@author GS-1496
	 * Goto requested report tab.
	 */
	public void gotoRequestedReportTab() {
		click(By.id(""));
	}

	/**
	 * Verify requested report page visibility.
	 *@author GS-1496
	 * @return true, if successful
	 */
	public boolean verifyRequestedReportPageVisibility() {
		return (isDisplayed(By.id("requested report grid")) && isEnabled(By.id("requested report tab"))
				&& (!isEnabled(By.id("schedule tab"))));
	}

	/**
	 * Verify breadcrumbs.
	 * @author GS-1496
	 * @return true, if successful
	 */
	public boolean verifyBreadcrumbs() {
		return isDisplayed(By.id(""));
	}

	/**
	 * Verify new report request button.
	 * @author GS-1496
	 * @return true, if successful
	 */
	public boolean verifyNewReportRequestButton() {
		return isDisplayed(By.id(""));
	}

	/**
	 * Verify tabs visibility.
	 * @author GS-1496
	 * @param tabName the tab name
	 * @param action the action
	 * @return true, if successful
	 */
	public boolean verifyTabsVisibility(String tabName, String action) {
		if (tabName.equalsIgnoreCase(SeleniumConst.REQUESTED_REPORTS.toString().replace("_", " ")) && action.equalsIgnoreCase(SeleniumConst.ENABLE)) {
			return isEnabled(By.id(""));
		} else if (tabName.equalsIgnoreCase(SeleniumConst.REQUESTED_REPORTS.toString().replace("_", " "))
				&& action.equalsIgnoreCase(SeleniumConst.DISABLE)) {
			return !(isEnabled(By.id("")));
		} else if (tabName.equalsIgnoreCase(SeleniumConst.SCHEDULE_REPORTS.toString().replace("_", " "))
				&& action.equalsIgnoreCase(SeleniumConst.ENABLE)) {
			return (isEnabled(By.id("")));
		} else if (tabName.equalsIgnoreCase(SeleniumConst.SCHEDULE_REPORTS.toString().replace("_", " "))
				&& action.equalsIgnoreCase(SeleniumConst.DISABLE)) {
			return !(isEnabled(By.id("")));
		}
		return false;
	}

	/**
	 * ==================================================================================
	 * Verification methods related to GRID (MIDDEL) AREA from EDB reporting
	 * application
	 * ==================================================================================.
	 *
	 * @param columnName the column name
	 * @param recordDetail the record detail
	 * @param action the action
	 * @author GS-1496
	 */

	public void gotoFindRecoredPerformedAction(String columnName, String recordDetail ,String action)
	{
		int size = driver.findElements(By.xpath("")).size();
		int columnNumber = 0;
		String tdText="";
		if (columnName.equals(ColumnName.User_ID.toString().replace("_", " "))) {
			columnNumber = 1;
		} else if (columnName.equals(ColumnName.Category)) {
			columnNumber = 2;
		} else if (columnName.equals(ColumnName.Report)) {
			columnNumber = 3;
		} else if (columnName.equals(ColumnName.Criteria)) {
			columnNumber = 4;
		} else if (columnName.equals(ColumnName.Time)) {
			columnNumber = 5;
		}

		for(int i=1;i<=size;i++)
		{
			tdText = getText(By.xpath(""));
			if(tdText.equalsIgnoreCase(recordDetail)){
				click(By.id("")); // click on action menu of that row
				click(By.xpath(action)); // click on action (Delete/Download)
				break;
			}
		}
	}

	/**
	 * Verify grid single recored.
	 * @author GS-1496
	 * @param columnName the column name
	 * @param recordDetail the record detail
	 * @return true, if successful
	 */
	public boolean verifyGridSingleRecored(String columnName, String recordDetail) {
		int size = driver.findElements(By.xpath("")).size();
		int columnNumber = 0;
		String tdText="";
		if (columnName.equals(ColumnName.Category)) {
			columnNumber = 1;
		} else if (columnName.equals(ColumnName.Report)) {
			columnNumber = 2;
		} else if (columnName.equals(ColumnName.Criteria)) {
			columnNumber = 3;
		} else if (columnName.equals(ColumnName.Time)) {
			columnNumber = 4;
		}

		for(int i=1;i<=size;i++)
		{
			tdText = getText(By.xpath(""));
			if(tdText.equalsIgnoreCase(recordDetail)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Verify grid globle search recored.
	 * @author GS-1496
	 * @param columnName the column name
	 * @param recordDetail the record detail
	 * @return true, if successful
	 */
	public boolean verifyGridGlobleSearchRecored(String columnName, String recordDetail) {
		ArrayList<String> columnList = new ArrayList<String>();
		int columnNumber = 0;
		String tdText="";
		if (columnName.equals(ColumnName.Category)) {
			columnNumber = 1;
		} else if (columnName.equals(ColumnName.Report)) {
			columnNumber = 2;
		} else if (columnName.equals(ColumnName.Criteria)) {
			columnNumber = 3;
		} else if (columnName.equals(ColumnName.Time)) {
			columnNumber = 4;
		}

		List<WebElement> allTD = driver.findElements(By.xpath(""));
		for(WebElement element: allTD)
		{
			columnList.add(element.getText().trim());
		}

		for(String elementText : columnList)
		{
			if(!elementText.equalsIgnoreCase(recordDetail))
			{
				return false;
			}
		}
		return false;
	}


	/**
	 * Verify grid recored status icon.
	 * @author GS-1496
	 * @param statusIcon the status icon
	 * @return true, if successful
	 */
	public boolean verifyGridRecoredStatusIcon(StatusIcon statusIcon) {
		if (statusIcon.equals(StatusIcon.Done)) {
			isDisplayed(By.xpath(""));
		} else if (statusIcon.equals(StatusIcon.Failed)) {
			isDisplayed(By.xpath(""));
		} else if (statusIcon.equals(StatusIcon.No_Recoreds.toString().replace("_", " "))) {
			isDisplayed(By.xpath(""));
		} else if (statusIcon.equals(StatusIcon.Ready)) {
			isDisplayed(By.xpath(""));
		} else if (statusIcon.equals(StatusIcon.Requested)) {
			isDisplayed(By.xpath(""));
		}
		return false;
	}

	/**
	 * Verify grid sorting.
	 * @author GS-1496
	 * @param columnName the column name
	 * @param sortingStyle the sorting style
	 * @return true, if successful
	 */
	public boolean VerifyGridSorting(String columnName, String sortingStyle) {
		ArrayList<String> beforeSortingDataList;
		int columnNumber = 0;
		if (columnName.equals(ColumnName.Category)) {
			columnNumber = 1;
		} else if (columnName.equals(ColumnName.Report)) {
			columnNumber = 2;
		} else if (columnName.equals(ColumnName.Criteria)) {
			columnNumber = 3;
		} else if (columnName.equals(ColumnName.Time)) {
			columnNumber = 4;
		}

		beforeSortingDataList = getList(columnNumber);
		Collections.sort(beforeSortingDataList);
		if (sortingStyle.equals(Sorting.Ascending)) {
			click(By.id(""));
			ArrayList<String> ascendingList = getList(columnNumber);
			return (ascendingList).equals(beforeSortingDataList);
		} else if (sortingStyle.equals(Sorting.Descending)) {
			click(By.id(""));
			ArrayList<String> descendingList = getList(columnNumber);
			Collections.reverse(beforeSortingDataList);
			return (descendingList).equals(beforeSortingDataList);
		}
		return false;
	}

	/**
	 * Verify grid heading.
	 * @author GS-1496
	 * @param headings the headings
	 * @return true, if successful
	 */
	public boolean verifyGridHeading(ArrayList<String> headings) {
		for (String col : headings) {
			if (!isDisplayed(By.xpath(col))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verify grid default size.
	 * @author GS-1496
	 * @param defaultSize the default size
	 * @return true, if successful
	 */
	public boolean verifyGridMaxRowSize(int defaultSize) {
		int size = getNumberOfElements(By.xpath(""));
		return size == defaultSize;
	}



	/**
	 * Verify blank page place holder msg.
	 * @author GS-1496
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean verifyBlankPagePlaceHolderMsg(String message) {
		return isDisplayed(By.xpath(message));
	}

	/**
	 * Verify delete popup.
	 *
	 * @return true, if successful
	 */
	public boolean verifyDeletePopup()
	{
		return isDisplayed(By.id(""));
	}

	/**
	 * Verify delete popup message.
	 * @author GS-1496
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean verifyDeletePopupMessage(String message)
	{
		return message.equalsIgnoreCase(driver.switchTo().alert().getText().trim());
	}

	/**
	 * Goto delete action.
	 * @author GS-1496
	 * @param action the action
	 */
	public void gotoDeleteAction(String action)
	{
		if(action.equalsIgnoreCase("ok"))
		{
			click(By.id(""));
		}
		else
		{
			click(By.id(""));
		}
	}

	public boolean verifyCurrentPage(String pageTitle)
	{
		return pageTitle.equalsIgnoreCase(driver.getTitle());
	}
	/**
	 * Gets the list.
	 * @author GS-1496
	 * @param columnNumber the column number
	 * @return the list
	 */
	private ArrayList<String> getList(int columnNumber) {
		List<WebElement> ElementsList = driver.findElements(By.xpath(""));
		ArrayList<String> listData = new ArrayList<String>();
		for (WebElement element : ElementsList) {
			listData.add(element.getText().trim());
		}
		return listData;
	}

	/**
	 * ====================================================================================
	 * Verification methods related to FOOTER (BOTTOM) AREA from EDB reporting
	 * application
	 * ====================================================================================.
	 *
	 * @param pageNum the page num
	 */

	public void clickOnPaginationNumber(String pageNum) {
		click(By.xpath(""));
	}

	/**
	 * Verify pagination navigation link.
	 * @author GS-1496
	 * @param navigationLink the navigation link
	 * @return true, if successful
	 */
	public boolean verifyPageNavigationLinkEnable(String navigationLink) {
		return isEnabled(By.xpath(navigationLink));
	}

}
