package com.company.micro.v1.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.company.micro.common.ErrorData;
import com.company.micro.common.MessageByLocale;
import com.company.micro.common.errors.BadRequestException;
import com.company.micro.common.validator.microValidatorMessage;
import com.company.micro.common.validator.schema.JsonSchema;
import com.company.micro.common.validator.schema.JsonSchemaFactory;
import com.company.micro.common.validator.schema.SchemaValidatorsConfig;
import com.company.micro.common.validator.schema.SpecVersion;
import com.company.micro.common.validator.schema.ValidationMessage;
import com.company.micro.helper.FormHelper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

/**
 * Contains business logic for product related operations.
 */
@Component
public class ProductValidator {

    private static final Logger errorLog = LoggerFactory.getLogger("error-log");
    private static final Logger appLog = LoggerFactory.getLogger("app-log");

    /**
     * Mock Site url.
     */
    @Value("${validation-service.url}")
    private String validationServiceUrl;

    /**
     * Localized messages.
     */
    private MessageByLocale messageByLocale;

    /**
     * Object Mapper.
     */
    private ObjectMapper objectMapper;

    /**
     * Form Helper.
     */
    private FormHelper formHelper;


    private RestTemplate restTemplate;

    /**
     * Custom validation message
     */
    private microValidatorMessage validatorMessage;

    @Autowired
    public ProductValidator(
            final MessageByLocale messageByLocale,
            final ObjectMapper objectMapper,
            final FormHelper formHelper,
            final RestTemplate restTemplate,
            final microValidatorMessage validatorMessage
    ) {
        this.messageByLocale = messageByLocale;
        this.objectMapper = objectMapper;
        this.formHelper = formHelper;
        this.restTemplate = restTemplate;
        this.validatorMessage = validatorMessage;
    }

    /**
     * Validate Product meta
     *
     * @param productData
     */
    public void validateProduct(
            final JsonNode productData,
            final Long tenantId,
            final String domain) {
        List<ErrorData> errors = new ArrayList<>();
        String productContent = null;

        try {
            productContent = objectMapper.writeValueAsString(productData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JSONObject productSchema = formHelper.getForms(tenantId, domain, "product");

        SchemaLoader loader = SchemaLoader.builder()
                .schemaJson(productSchema)
                .draftV7Support()
                .build();

        Schema schema = loader.load().build();

        try {
            schema.validate(new JSONObject(productContent));
        } catch (ValidationException e) {
            appLog.info("{}", keyValue("productValidationError", e.toJSON()));

            List<ValidationException> validationExceptions = e.getCausingExceptions();
            e.getCausingExceptions().stream()
                    .forEach((levelOne) -> {
                        addErrors(errors, levelOne);
                        levelOne.getCausingExceptions().stream().forEach((levelTwo) -> {
                            addErrors(errors, levelTwo);
                            levelTwo.getCausingExceptions().stream().forEach((levelThree) -> {
                                addErrors(errors, levelThree);
                            });
                        });
                    });
            throw new BadRequestException(e.getMessage(), errors, e.getPointerToViolation());
        }
    }

    public void validateProductV1 (
            final JsonNode productData,
            final Long tenantId,
            final String domain
    ){
        String productSchema = formHelper.getFormString(tenantId, domain, "product");
        SchemaValidatorsConfig config = new SchemaValidatorsConfig();
        config.setFailFast(false);
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        JsonSchema schema = schemaFactory.getSchema(productSchema, config);
        Set<ValidationMessage> validationResult = schema.validate(productData);

        // print validation errors
        if (((Set<?>) validationResult).isEmpty()) {
            appLog.info("No Validation errors");
            return;
        }
        List<ErrorData> errors = new ArrayList<>();
            validationResult.forEach(vm -> {
                errors.add(validatorMessage.getMessage(vm));
            });

        throw new BadRequestException("Validation error", errors, "product");
    }

    public void validateProductAPI(
            final JsonNode productData,
            final Long tenantId,
            final String domain) {

        validatePost(tenantId, domain, productData);
    }

    public void validatePost(
            final Long tenantId, final String domain, final JsonNode dto) {
        List<ErrorData> errors = new ArrayList<>();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", tenantId.toString());
        headers.set("domain", domain);
        headers.set("entity", "product");
        final HttpEntity<?> httpEntity = new HttpEntity<>(dto, headers);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(
                validationServiceUrl.concat("/v1/validators"), HttpMethod.POST, httpEntity, String.class);

        if(HttpStatus.OK.value() == responseEntity.getStatusCodeValue()) {
            return;
        }

        appLog.info("{}", keyValue("validation service response : ", responseEntity));
        final JSONObject responseBody = new JSONObject(responseEntity.getBody());

        final JSONObject error = responseBody.getJSONObject("error");
        final JSONArray errorDetails = error.getJSONArray("details");

        for(Object errorDetail : errorDetails) {
            final JSONObject errorObj = new JSONObject(errorDetail.toString());
            HashMap<String,String> paramMap = new Gson().fromJson(errorObj.getJSONObject("params").toString(),
                    new TypeToken<HashMap<String, String>>(){}.getType());
            errors.add(
                    new ErrorData.Builder()
                            .code(errorObj.getString("keyword"))
                            .message(errorObj.getString("message"))
                            .path(errorObj.getString("dataPath"))
                            .target(errorObj.getString("schemaPath"))
                            .key(paramMap)
                            .build()
            );
        }
        throw new BadRequestException("Validation error", errors, "product");
    }

    /**
     * Add Errors
     *
     * @return JSONObject
     */
    public List<ErrorData> addErrors(List<ErrorData> errors, ValidationException validationException) {
        errors.add(
                new ErrorData.Builder()
                        .code(validationException.getKeyword())
                        .target(validationException.getPointerToViolation())
                        .path(validationException.getSchemaLocation())
                        //.key(validationException.getKey())
                        .message(validationException.getMessage())
                        .build()
        );

        return errors;
    }

    /**
     * Get Project Validation schema json
     *
     * @return JSONObject
     */
    public JSONObject getProductValidationSchema() {

        JSONObject productSchema;
        String schemaContent = null;
        try {
            schemaContent = new String(Files.readAllBytes(Paths.get(
                    ProductValidator.class.getResource("/validations/productDynamicSchema.json").getPath()
            )));

        } catch (IOException e) {
            errorLog.error("Caught Parsing exception for product scheam {} ", e);
            throw new RuntimeException(e);
        }
        productSchema = new JSONObject(schemaContent);

        return productSchema;
    }


}
