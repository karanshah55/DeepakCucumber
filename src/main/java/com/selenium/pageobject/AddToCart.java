package com.selenium.pageobject;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.utility.selenium.BasePageObject;

public class AddToCart extends BasePageObject{
	
	// private static final By userName=By.xpath("//input[@id='email']");
	
	public void addToCart(String productName){
		System.out.println(productName); 
		timeInterval(2);
		Actions build = new Actions(driver);
		WebElement element  = driver.findElement(By.xpath("(//ul[@class='product_list grid row']//img[@title='"+productName+"'])[1]"));
		build.moveToElement(element).pause(Duration.ofMillis(4000)).build().perform();		
		timeInterval(2);
		click(By.xpath("(//ul[@class='product_list grid row']//img[@title='"+productName+"']/../../../../div//a/span[text()='Add to cart'])[1]"));
		//click(By.xpath("//a[@title='Add to cart']"));
		//click(By.xpath("(//ul[@class='product_list grid row']//img[@title='"+productName+"'])[1]//span[contains(text(),'Add to cart')]"));
		timeInterval(3);
		click(By.xpath("//span[@title='Continue shopping']//span[1]"));
		timeInterval(3);
	}

	public boolean checkProductWithPrice(String price, String product){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,-2500)", "");
		timeInterval(1);
		mouseOver(By.xpath("//a[@title='View my shopping cart']"));
		timeInterval(1);
		System.out.println("//dt//span[@class='price' and contains(text(),'$"+price+"')]/..//a[@title='"+product+"']");
		System.out.println("Is display : "+isDisplayed(By.xpath("//dt//span[@class='price' and contains(text(),'$"+price+"')]/..//a[contains(@title,'"+product+"')]")));
		return isDisplayed(By.xpath("//dt//span[@class='price' and contains(text(),'$"+price+"')]/..//a[contains(@title,'"+product+"')]"));
	}//what is the alternative to this method
	
	public boolean checkTotal(float totalPrice){
		return isDisplayed(By.xpath("//span[@class='price cart_block_total ajax_block_cart_total' and contains (text(),'$"+totalPrice+"')]"));
	}
	
	
	
}
