Feature: To retrieve the product with product details

  Scenario: retrieve the product with product id
    Given the product saved with product name "product 1" and product id 100
    When the client calls GET "/product/{productId}" with product id as 100
    Then the client receives status code of 200 for get product
    And the response contains product name "product 1" for get

