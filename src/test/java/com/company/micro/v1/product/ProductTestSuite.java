package com.company.micro.v1.product;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Micro API Suite demo")
//@SelectPackages("com.company.micro.v1.product")
@SelectClasses({ProductRepoTest.class, ProductControllerIT.class})
public class ProductTestSuite {
}
