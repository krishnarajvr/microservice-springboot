Feature: To save the product with details

  Scenario: client makes call to POST /products to save the product
    Given the product with name "Prouct 1" and  code 1
    When the client calls "products/1" with the given details
    Then the client receives status code of 200
    And the response contains product name "Product 1"
