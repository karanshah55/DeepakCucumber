package com.cucumber.stepdefinitions;

import java.util.List;

import org.testng.asserts.SoftAssert;

import com.selenium.pageobject.HomePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class SearchDress {
	
	HomePage homePage = new HomePage();
	SoftAssert verify = new SoftAssert();
	@Given("^User will enter \"([^\"]*)\" keyword into the global search text box and click find$")
	public void user_will_enter_Dress_keyword_into_the_global_search_text_box_and_click_find(String keyword) throws Throwable {
		homePage.enterTextInSearchBox(keyword);
	}

	@Given("^User browse through all the shown result and collects the data$")
	public void user_browse_through_all_the_shown_result_and_collects_the_data() throws Throwable {
		List<String> searchItem = homePage.collectAllElements();
		for(String item : searchItem)
			verify.assertTrue(item.contains("Dress"));
	}

	@Then("^User will check whether all the result has Dress keyword$")
	public void user_will_check_whether_all_the_result_has_Dress_keyword() throws Throwable {
		homePage.printPrice();
	}

}
