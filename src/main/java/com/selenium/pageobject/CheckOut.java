package com.selenium.pageobject;

import org.openqa.selenium.By;

import com.utility.selenium.BasePageObject;

public class CheckOut extends BasePageObject{

	ProductPurchasePage prodPage = new ProductPurchasePage();
	
	private static final By magicMouseLocator = By.xpath("//a[contains(text(),'Magic Mouse')]");
	private static final By magicMousePriceLocator = By.xpath("//span[contains(text(),'$150.00')]");
	
	public boolean isMagicMouseLocatorPresent(){
		if((isElementPresent(magicMouseLocator)== true)){
			return true;
		}else 
			return false;
	}
	
	public boolean ismagicMousePriceLocatorPresent(){
		if((isElementPresent(magicMousePriceLocator)== true)){
			return true;
		}else 
			return false;
	}
	
	public int calculateProdTotalPrice(String prodName, int qunatity){
		int prodPrice = prodPage.getProductPrice(prodName);
		int totalPrice = qunatity * prodPrice;
		return totalPrice;
	}
}
