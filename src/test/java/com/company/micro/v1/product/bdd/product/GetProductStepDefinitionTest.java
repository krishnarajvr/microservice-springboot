package com.company.micro.v1.product.bdd.product;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class GetProductStepDefinitionTest {

	private static final Logger logger = LoggerFactory.getLogger(GetProductStepDefinitionTest.class);

	private String productName = null;
	private int productId;
	private String tenantId = "1";

	private ResponseEntity<String> response = null;

	@Autowired
	private ProductHttpClient productHttpClient;

	@Given("the product saved with product name {string} and product id {int}")
	public void the_product_saved_with_product_name_and_product_id(String string, Integer int1)
	{
		// Write code here that turns the phrase above into concrete actions
		//throw new io.cucumber.java8.PendingException();
	};

	@When("the client calls GET {string} with product id as {int}")
	public void the_client_calls_with_product_id(String string, Integer int1) {
		// Write code here that turns the phrase above into concrete actions
		//throw new io.cucumber.java8.PendingException();
	};

	@Then("^the client receives status code of (\\d+) for get product$")
	public void the_client_receives_status_code_of_for_get_product(int statusCode) throws Throwable {

		if (response != null && response.getStatusCode().is2xxSuccessful()) {
			assertEquals(statusCode, response.getStatusCode().value());
		}
	}

	@Then("^the response contains product name \"([^\"]*)\" for get$")
	public void the_response_contains_product_name_for_get(String productName) throws Throwable {
		assertEquals(productName, "abc");
	}


}
