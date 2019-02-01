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

import com.utility.constant.SeleniumConst.ColumnName;
import com.utility.constant.SeleniumConst.Sorting;
import com.utility.selenium.BasePageObject;

// TODO: Auto-generated Javadoc
/**
 * The Class ScheduleReportPage.
 */
public class ScheduleReportPage extends BasePageObject<T> {
	
	String loginID="lgn_txt";
	String loingName="name";
	String loginBtnXpatj=".//[@id='btn']";
	/**
	 * ==================================================================================
	 * Verification methods related to HEADERS (TOP) AREA from EDB reporting
	 * application
	 * ==================================================================================.
	 */

	public void gotoScheduleReportTab() {
		click(By.id(loginID));
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


	public void gotoFindRecoredPerformedAction(ColumnName columnName, String recordDetail, String action) {
		int size = driver.findElements(By.xpath("")).size();
		int columnNumber = 0;
		String tdText = "";
		if (columnName.equals(ColumnName.User_ID)) {
			columnNumber = 1;
		} else if (columnName.equals(ColumnName.Report)) {
			columnNumber = 2;
		} else if (columnName.equals(ColumnName.Description)) {
			columnNumber = 3;
		} else if (columnName.equals(ColumnName.Criteria)) {
			columnNumber = 4;
		} else if (columnName.equals(ColumnName.Frequancy)) {
			columnNumber = 5;
		} else if (columnName.equals(ColumnName.Runtime)) {
			columnNumber = 6;
		}

		for (int i = 1; i <= size; i++) {
			tdText = getText(By.xpath(""));
			if (tdText.equalsIgnoreCase(recordDetail)) {
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
	public boolean verifyGridSingleRecored(ColumnName columnName, String recordDetail) {
		int size = driver.findElements(By.xpath("")).size();
		int columnNumber = 0;
		String tdText = "";
		if (columnName.equals(ColumnName.User_ID)) {
			columnNumber = 1;
		} else if (columnName.equals(ColumnName.Report)) {
			columnNumber = 2;
		} else if (columnName.equals(ColumnName.Description)) {
			columnNumber = 3;
		} else if (columnName.equals(ColumnName.Criteria)) {
			columnNumber = 4;
		} else if (columnName.equals(ColumnName.Frequancy)) {
			columnNumber = 5;
		} else if (columnName.equals(ColumnName.Runtime)) {
			columnNumber = 5;
		}

		for (int i = 1; i <= size; i++) {
			tdText = getText(By.xpath(""));
			if (tdText.equalsIgnoreCase(recordDetail)) {
				return true;
			}
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
	 * Verify grid sorting.
	 * @author GS-1496
	 * @param columnName the column name
	 * @param sortingStyle the sorting style
	 * @return true, if successful
	 */
	public boolean VerifyGridSorting(ColumnName columnName, Sorting sortingStyle) {
		ArrayList<String> beforeSortingDataList;
		int columnNumber = 0;
		if (columnName.equals(ColumnName.User_ID)) {
			columnNumber = 1;
		} else if (columnName.equals(ColumnName.Report)) {
			columnNumber = 2;
		} else if (columnName.equals(ColumnName.Description)) {
			columnNumber = 3;
		} else if (columnName.equals(ColumnName.Criteria)) {
			columnNumber = 4;
		} else if (columnName.equals(ColumnName.Frequancy)) {
			columnNumber = 5;
		} else if (columnName.equals(ColumnName.Runtime)) {
			columnNumber = 5;
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
	 * Goto delete action.
	 * @author GS-1496
	 * @param action the action
	 */
	public void gotoDeleteAction(String action) {
		if (action.equalsIgnoreCase("ok")) {
			click(By.id(""));
		} else {
			click(By.id(""));
		}
	}

	/**
	 * Verify delete popup message.
	 * @author GS-1496
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean verifyDeletePopupMessage(String message) {
		return message.equalsIgnoreCase(driver.switchTo().alert().getText().trim());
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


}
