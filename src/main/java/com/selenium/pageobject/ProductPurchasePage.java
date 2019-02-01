package com.selenium.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.utility.selenium.BasePageObject;

public class ProductPurchasePage extends BasePageObject{

	private static final By productCategory = By.xpath("//a[text()='Product Category']");
	private static final By checkOutLink = By.xpath("//a[@title='Checkout']");
	
	
	public boolean verifyPageTitle(String pageTitle){
		return isDisplayed(By.xpath("//h1[@class='entry-title' and text()='']"));
	}
	
	
	public void addToCart(By by){
		click(by);
	}
	
	public boolean compareProductPrice(String productName, String productPrice){
		String tempPrice = getText(By.xpath("//div[@id='default_products_page_container']//div[contains(@class,'default_product_display product_view')]//img[@alt='"+productName+"']/../../..//span[contains(@class,'currentprice pricedisplay product_price')]"));
		String[] price = tempPrice.split("$");
		//String price1 = price[0];
		String price2 = price[1];
		return productPrice.equals(price2); 
	}
	
	public int getProductPrice(String productName){
		String tempPrice = getText(By.xpath(""));
		String price[] = tempPrice.split("$");
		String tempprodPrice = price[1];
		int prodPrice = Integer.parseInt(tempprodPrice);	
		return prodPrice;
	}
	
	
	
	public void checkOut(){
		click(checkOutLink);
	}
	
	
	public void goToProductPage(String productPageName){
		mouseOver(productCategory);
		click(By.xpath("//a[contains(text(),'"+productPageName+"')]"));
	}
	
}
