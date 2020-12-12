package com.company.micro.v1.product.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Tag("Scenario 1")
@CucumberOptions(features = "src/test/resources/features/suite/product", plugin = {
		"html:target/cucumber-report" }, monochrome = true)
public class ProductScenarioTest {

}
