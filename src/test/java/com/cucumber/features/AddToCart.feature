@AddProductsFunctionality1
Feature: Adding products to Cart and Verify the added products

  
  Scenario: Login
    Given User enters username and password
      | karan | karan |
  Scenario: Search Dress in the search bar
    And User will enter "Dress" keyword into the global search text box and click find
    And User browse through all the shown result and collects the data
    And User will check whether all the result has Dress keyword
    And User will Add following products to the cart
      | Dress Name           | Price |
      | Printed Summer Dress | 28.98 |
      | Printed Dress        | 50.99 |
    Then User will verify the following added products to the cart and their pricing
    	| Dress Name           | Price |
      | Printed Summer Dress | 28.98 |
      | Printed Dress        | 50.99 |

