package com.selenium.pageobject;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.utility.selenium.BasePageObject;

public class HomePage extends BasePageObject{

	private static final By searchTextBox=By.id("search_query_top");
	private static final By searchButton=By.xpath("//button[@name='submit_search']");
	private static final By printedSummerDressPrice=By.xpath("(//div[@class='product-image-container']//span[@class='price product-price'])[1]");
	private static final By printedDressPrice=By.xpath("(//div[@class='product-image-container']//span[@class='price product-price'])[2]");
	private static final By printedSummerDress=By.xpath("/html[1]/body[1]/div[1]/div[2]/div[1]/div[3]/div[2]/ul[1]/li[1]/div[1]/div[1]/div[1]/a[1]/img[1]");
	private static final By printedDress=By.xpath("/html[1]/body[1]/div[1]/div[2]/div[1]/div[3]/div[2]/ul[1]/li[2]/div[1]/div[1]/div[1]/a[1]/img[1]");

	public void enterTextInSearchBox(String keyword){
		click(searchTextBox);
		sendKeys(searchTextBox,keyword);
		click(searchButton);
	}
	
	public List<String> collectAllElements(){
		List<String> itemName = new ArrayList();
		List<WebElement> listOfProducts = driver.findElements(By.xpath("//img[@width='250']"));
		 System.out.println("Number of products : " +listOfProducts.size());

		    for (int i=0; i<listOfProducts.size();i++){
		    	int j=i+1;
		      System.out.println(j+" product : " + listOfProducts.get(i).getAttribute("title"));
		      //String isDressPresent = listOfProducts.get(i).getAttribute("title");
		      itemName.add(listOfProducts.get(i).getAttribute("title"));
		      if((listOfProducts.get(i).getAttribute("title").contains("Dress"))){
		    	  System.out.println("contains Dress");
		      }else{
		    	  System.out.println("Does not contain Dress");
		      }
		    }
		    return itemName;
	}
	
	public void printPrice(){
		mouseOver(printedSummerDress);
		String price1=driver.findElement(printedSummerDressPrice).getText();
		String[] price1Arr1 = price1.split("\\$"); 
        for (String a : price1Arr1) 
            System.out.println(a); 
		mouseOver(printedDress);
		String price2=driver.findElement(printedDressPrice).getText();
		String[] price1Arr2 = price2.split("\\$"); 
        for (String a : price1Arr2) 
            System.out.println(a);
	}
}
