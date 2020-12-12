package com.company.micro.v1.product.bdd.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.company.micro.helper.MockHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class ProductHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(MockHelper.class);

    private final String SERVER_URL = "http://localhost";
    private final String THINGS_ENDPOINT = "/v1/products";

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    private String thingsEndpoint() {
        return SERVER_URL + ":" + port + THINGS_ENDPOINT;
    }

    private String baseEndPoint() { return SERVER_URL + ":" + port + "/v1/";}

    public int put(final String something) {
        final int a = restTemplate.postForEntity(thingsEndpoint(), something, Void.class).getStatusCodeValue();
        return a;
    }

    public void getContents(String tenantId) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("tenantId", tenantId);

        LOG.info(thingsEndpoint());
        try {

            ResponseEntity<String> entity = restTemplate.exchange(
                    thingsEndpoint(), HttpMethod.GET, new HttpEntity<String>(headers),
                    String.class);

            LOG.info("Entity response : " + entity.toString());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<JsonNode> getProductDetails(String path, String tenantId) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("tenantId", tenantId);
        ResponseEntity<JsonNode> entity = null;

        LOG.info(thingsEndpoint());
        try {

            entity = restTemplate.exchange(
                    baseEndPoint() + path, HttpMethod.GET, new HttpEntity<JsonNode>(headers),
                    JsonNode.class);
            LOG.info("Entity response : " + entity.toString());

        }catch (Exception e) {
            LOG.info("Exception : " + e.toString());
        }
        return entity;
    }

    public void clean() {
        restTemplate.delete(thingsEndpoint());
    }


}
