package com.company.micro.v1.product.bdd.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.company.micro.entity.Product;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaveProductStepDefinitionTest {

	private static final Logger logger = LoggerFactory.getLogger(SaveProductStepDefinitionTest.class);

	private String productName = null;
	private int productId;
	private String tenantId = "1";

	private ResponseEntity<JsonNode> response = null;

	@Autowired
	private ProductHttpClient productHttpClient;

	@Given("the product with name {string} and  code {int}")
	public void the_product_with_name_and_code(String productName, Integer productId) {
		if (logger.isInfoEnabled()) {
			logger.info("Customer to be saved with product name {} and product id {}", productName, productId);
		}
		this.productId = productId;
		this.productName = productName;
	}

	@When("^the client calls \"([^\"]*)\" with the given details$")
	public void the_client_calls_product_save_with_the_given_details(String path) throws Throwable {
		if (logger.isInfoEnabled()) {
			logger.info("he client calls {} with the given details", path);
		}
		response = productHttpClient.getProductDetails(path, tenantId);
	}

	@Then("^the client receives status code of (\\d+)$")
	public void the_client_receives_status_code_of(int statusCode) throws Throwable {

		if (response != null && response.getStatusCode().is2xxSuccessful()) {
			assertEquals(statusCode, response.getStatusCode().value());
		}
	}

	@Then("^the response contains product name \"([^\"]*)\"$")
	public void the_response_contains_product_name(String productName) throws Throwable {

		if (response != null && response.getStatusCode().is2xxSuccessful()) {
			JsonNode product = response.getBody().get("data").get("product");
			assertEquals(productName, product.get("name").asText());
		}
	}

	private Product createProduct(
			final Long tenantId,
			final String code,
			final String name,
			final BigDecimal price
	) {
		final LocalDateTime now = LocalDateTime.now();

		return Product
				.builder()
				.tenantId(tenantId)
				.code(code)
				.name(name)
				.price(price)
				.createdTime(now)
				.updatedTime(now)
				.build();
	}

}
