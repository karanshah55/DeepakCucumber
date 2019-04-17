package com.cucumber.stepdefinitions;

import gherkin.formatter.model.DataTableRow;

import java.util.List;

import org.testng.Assert;

import com.selenium.pageobject.AutomationLoginPage;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class AddToCart {

	AutomationLoginPage login = new AutomationLoginPage();
	com.selenium.pageobject.AddToCart cart = new com.selenium.pageobject.AddToCart();
	List<DataTableRow> productName_Price;
	
	@And("^User will Add following products to the cart$")
	public void user_will_Add_following_to_the_cart(DataTable products) throws Throwable {
		productName_Price = products.getGherkinRows();
	   for(int i=1;i<productName_Price.size();i++){
		   DataTableRow row = productName_Price.get(i);
		   cart.addToCart(row.getCells().get(0));
	   }
	}
	
	
	@Then("^User will verify the following added products to the cart and their pricing$")
	public void user_will_verify_the_following_added_products_to_the_cart_and_their_pricing(DataTable products) throws Throwable {
		float sum = 0;
		productName_Price = products.getGherkinRows();
		for(int i=1;i<productName_Price.size();i++){
			   DataTableRow row = productName_Price.get(i);
			   sum = sum + Float.parseFloat(row.getCells().get(1));
			   Assert.assertTrue(cart.checkProductWithPrice(row.getCells().get(1), row.getCells().get(0)));
		   }
		sum = sum+2;
		Assert.assertEquals(cart.checkTotal(sum),true);
		
	}
	
}
