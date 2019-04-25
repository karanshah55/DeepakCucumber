package com.cucumber.stepdefinitions;

import static org.testng.Assert.assertTrue;

import org.testng.Assert;

import com.selenium.pageobject.AccessoriesPage;
import com.selenium.pageobject.CheckOut;
import com.selenium.pageobject.ProductPurchasePage;
import com.utility.selenium.BaseTestScript;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CheckOutStep extends BaseTestScript{
	

	ProductPurchasePage prodPurchase = new ProductPurchasePage();
	AccessoriesPage acc = new AccessoriesPage(); 
	CheckOut chckOut = new CheckOut();
	
		@Given("^User will navigate to the Accessories page$")
		public void user_will_navigate_to_the_Accessories_page() throws Throwable {
			prodPurchase.goToProductPage("Accessories");		
			//verify page title
		}

		@Given("^User will do AddToCart products$")
		public void user_will_do_AddToCart_products() throws Throwable {
			acc.magicMouseAddToCart();
			acc.appleTvAddToCart();
		}

		@Given("^User will click on Checkout link$")
		public void user_will_click_on_Checkout_link() throws Throwable {
			prodPurchase.checkOut();
		}

		@Then("^User will see the same selected products and correct total price$")
		public void user_will_see_the_same_selected_products_and_correct_total_price() throws Throwable {
			//assertTrue(driver.getTitle().contains("Checkout | ONLINE STORE"));
			Assert.assertEquals("Checkout | ONLINE STORE", driver.getTitle());
			boolean magicMousePresent = chckOut.isMagicMouseLocatorPresent();
			Assert.assertTrue(magicMousePresent == true);
			boolean magicMousePricePresent = chckOut.ismagicMousePriceLocatorPresent();
			Assert.assertTrue(magicMousePricePresent == true);
			
		}
}