@CheckOut
Feature: Checkout of the products

@Accessories
Scenario: Select products from Accessories Page and Checkout
Given User will provide username as "<username>"
	And User will provide password as "<password>"
	And User will click Login
	And User will navigate to the Accessories page
	And User will do AddToCart products
	And User will click on Checkout link
	Then User will see the same selected products and correct total price

