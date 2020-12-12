package com.company.micro.v1.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.company.micro.Application;
import com.company.micro.entity.Product;
import com.company.micro.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class ProductControllerIT {

    private static final String PRODUCTS_URL = "/v1/products";
    private static final String TENANT_ID_HEADER_LABEL = "tenantId";
    private static final Long TENANT_ID = 1l;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private ObjectNode jsonConfig;

    @Autowired
    private ObjectMapper jsonMapper;

    @BeforeEach
    public void setUp() throws IOException {
        this.jsonConfig =jsonMapper.readValue(new File(
                ProductControllerIT.class.getResource("/stub/core/product/product.json").getPath()) , ObjectNode.class);
    }

    @AfterEach
    public void tearDown(){

    }

    @Test
    public void fetchProductByIdWithoutAuthHeadersShouldReturn403() throws Exception {
        mockMvc
                .perform(get("/v1/products/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error.code").value(HttpStatus.FORBIDDEN.name()))
                .andReturn();
    }

    @Test
    public void fetchProductByIdWithNonExistingProductIdShouldReturn404() throws Exception {
        mockMvc
                .perform(get("/v1/products/0")
                        .header("tenantId", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error.code").value(HttpStatus.NOT_FOUND.name()))
                .andReturn();
    }

    @Test
    @Transactional
    public void fetchProductByIdWithValidProductIdShouldReturn200() throws Exception {
        final Long tenantId = new Random().nextLong();

        final Product testProduct = createProduct(tenantId,
                "testProduct", "testProduct", BigDecimal.valueOf(20.0));
        productRepository.save(testProduct);

        mockMvc
                .perform(get("/v1/products/" + testProduct.getId())
                        .header("tenantId", tenantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").isEmpty())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.product").isNotEmpty())
                .andExpect(jsonPath("$.data.product.id").value(testProduct.getId()))
                .andExpect(jsonPath("$.data.product.name").value(testProduct.getName()))
                .andReturn();

        productRepository.delete(testProduct);
    }

    @Test
    @Transactional
    public void saveProductWithEmptyCodeShouldReturn400() throws Exception {
        final Long tenantId = new Random().nextLong();

        final Product testProduct = createProduct(tenantId,
                "", "product 1", BigDecimal.valueOf(20.0));
                mockMvc
                .perform(post(PRODUCTS_URL)
                        .header(TENANT_ID_HEADER_LABEL, TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(Collections.singletonMap("product", testProduct)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isNotEmpty())
                .andReturn();

    }

    @Test
    @Transactional
    @Tag("xyz")
    @Tag("saveProductWithExistingCodeShouldReturn409")
    public void saveProductWithExistingCodeShouldReturn409() throws Exception {
        final Long tenantId = new Random().nextLong();

        JsonNode testScenario = jsonConfig.get("saveProductWithExistingCodeShouldReturn409");
        final Product testProduct = createProduct(tenantId,
                testScenario.get("input").get("product").get("code").toString(),
                testScenario.get("input").get("product").get("name").toString(),
                BigDecimal.valueOf(testScenario.get("input").get("product").get("price").asDouble()));
        mockMvc
                .perform(post(PRODUCTS_URL)
                        .header(TENANT_ID_HEADER_LABEL, TENANT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(Collections.singletonMap("product", testProduct)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isNotEmpty())
                .andExpect(jsonPath("$.error.code").isNotEmpty())
                .andExpect(jsonPath("$.error.code")
                        .value(testScenario.get("output").get("error").get("code").asText()))
                .andReturn();

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
