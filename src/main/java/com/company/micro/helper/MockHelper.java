package com.company.micro.helper;

import com.company.micro.common.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

/**
 * Represents the moce helper for api testing.
 */
@Component
public class MockHelper {

    /**
     * Mock Api list.
     */
    private static final Map<String, Boolean> mockApis = new HashMap<>();

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockHelper.class);

    /**
     * Object Mapper Class.
     */
    private ObjectMapper objectMapper;

    /**
     * Rest Template.
     */
    private RestTemplate restTemplate;

    /**
     * Mock Site url.
     */
    @Value("${application.mock-server}")
    private String mockServerUrl;

    /**
     * Constructor injections.
     *
     * @param objectMapper
     */
    @Autowired
    public MockHelper(
            final ObjectMapper objectMapper,
            final RestTemplate restTemplate
    ) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Initialize MOC api enabled list.
     */
    @PostConstruct
    public void init() {
        mockApis.put("get_products", true);
        mockApis.put("post_products", true);
    }

    /**
     * Check MOC enabled or not.
     *
     * @param enpoint String Api endpoint
     * @param method  String HTTP method
     * @return Boolean is enabled or not
     */
    public Boolean isEnabled(final String enpoint, final String method) {
        final String key = method.toLowerCase().concat("_").concat(enpoint);

        return mockApis.containsKey(key) && mockApis.get(key);
    }

    /**
     * Mock Get Request.
     *
     * @param uri String
     * @return ResponseEntity
     */
    public ResponseEntity<APIResponse> mockGet(
            final String uri) {
        final HttpHeaders headers = new HttpHeaders();
        final HttpEntity<String> httpEntity = new HttpEntity<>("", headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(
                mockServerUrl.concat(uri), HttpMethod.GET, httpEntity, String.class);

        ResponseEntity<APIResponse> response = null;
        try {
            response = ResponseEntity.status(responseEntity.getStatusCode())
                    .body(objectMapper.readValue(responseEntity.getBody(), APIResponse.class));
        } catch (JsonProcessingException e) {
            LOG.error("error reading data", e);
        }

        // Log the api data
        logApiData("Data from mock server get api", uri, httpEntity, response);

        return response;
    }

    /**
     * Mock Post Request.
     *
     * @param uri String
     * @param dto ObjectNode
     * @return ResponseEntity
     */
    public ResponseEntity<APIResponse> mockPost(
            final String uri, final ObjectNode dto) {

        final HttpHeaders headers = new HttpHeaders();
        final HttpEntity<?> httpEntity = new HttpEntity<>(dto, headers);

        final ResponseEntity<APIResponse> responseEntity = restTemplate.exchange(
                mockServerUrl.concat(uri), HttpMethod.POST, httpEntity, APIResponse.class);

        LOG.info("{}", keyValue("Mock Response Before : ", responseEntity));

        ResponseEntity<APIResponse> response = null;

        // Log the api data
        logApiData("Data from mock server post api", uri, httpEntity, response);

        return responseEntity;
    }

    /**
     * Mock Patch Request.
     *
     * @param uri String
     * @param dto ObjectNode
     * @return ResponseEntity
     */
    public ResponseEntity<APIResponse> mockPatch(
            final String uri,
            final ObjectNode dto) {

        final HttpHeaders headers = new HttpHeaders();
        final HttpEntity<?> httpEntity = new HttpEntity<>(dto, headers);

        final ResponseEntity<APIResponse> responseEntity = restTemplate
                .exchange(mockServerUrl.concat(uri), HttpMethod.PATCH, httpEntity, APIResponse.class);

        // Log the api data
        logApiData("Data from mock server patch api", uri, httpEntity, responseEntity);

        return responseEntity;
    }

    /**
     * Mock Delete Request.
     *
     * @param uri String
     * @param dto ObjectNode
     * @return ResponseEntity
     */
    public ResponseEntity<APIResponse> mockDelete(
            final String uri,
            final ObjectNode dto
    ) {
        final HttpHeaders headers = new HttpHeaders();
        final HttpEntity<?> httpEntity = new HttpEntity<>(dto, headers);

        final ResponseEntity<APIResponse> responseEntity = restTemplate
                .exchange(mockServerUrl.concat(uri), HttpMethod.DELETE, httpEntity, APIResponse.class);

        // Log the api data
        logApiData("Data from mock server delete api", uri, httpEntity, responseEntity);

        return responseEntity;
    }

    /**
     * Method to log api input and response data.
     *
     * @param message        String
     * @param url            String
     * @param requestEntity  HttpEntity input data
     * @param responseEntity ResponseEntity reqponse entity
     */
    private void logApiData(
            final String message,
            final String url,
            final HttpEntity<?> requestEntity,
            final ResponseEntity<APIResponse> responseEntity
    ) {
        LOG.info("{}, {}, {}, {}",
                keyValue("message", message),
                keyValue("requestUrl", url),
                keyValue("request", requestEntity),
                keyValue("response", responseEntity));
    }
}
