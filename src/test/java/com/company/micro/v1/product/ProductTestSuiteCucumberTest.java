package com.company.micro.v1.product;


import com.company.micro.v1.product.bdd.ProductScenarioTest;
import org.junit.jupiter.api.Tag;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Micro Suite Cucumber")
//@SelectPackages("com.company.micro.v1.product")
@SelectClasses({ProductScenarioTest.class})
@Tag("SmokeTest")
public class ProductTestSuiteCucumberTest {
}
