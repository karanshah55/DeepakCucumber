package com.selenium.pageobject;

import org.openqa.selenium.By;

import com.utility.selenium.BasePageObject;

public class AccessoriesPage extends BasePageObject{
	
	public static final By magicMouseAddToCart=By.xpath("//form[@name='product_40']//input[@value='Add To Cart']");
	private static final By appleTvAddToCart=By.xpath("//form[@name='product_89']//input[@value='Add To Cart']");
	private static final By magicMousePrice=By.xpath("//span[@class='currentprice pricedisplay product_price_40']");
	private static final By appleTvPrice=By.xpath("//span[@class='currentprice pricedisplay product_price_89']");
	
	ProductPurchasePage prodPurchase = new ProductPurchasePage();
	
	public void magicMouseAddToCart(){
		prodPurchase.addToCart(magicMouseAddToCart);
	}
	
	public void appleTvAddToCart(){
		prodPurchase.addToCart(appleTvAddToCart);
	}
}
