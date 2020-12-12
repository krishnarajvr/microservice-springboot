package com.company.micro.v1.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.company.micro.common.validator.microValidatorMessage;
import com.company.micro.common.validator.schema.JsonSchema;
import com.company.micro.common.validator.schema.JsonSchemaException;
import com.company.micro.common.validator.schema.JsonSchemaFactory;
import com.company.micro.common.validator.schema.SchemaValidatorsConfig;
import com.company.micro.common.validator.schema.SpecVersion;
import com.company.micro.common.validator.schema.ValidationMessage;
import com.company.micro.v1.form.projection.FormProjection;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.Map;
import java.util.HashMap;

/**
 * Service for Common User related APIs (v1).
 */
@Service("FormServiceV1")
public class FormService {

    /**
     * Repository for Common User Operations.
     */
    private FormRepo repository;

    protected static final Logger log = LoggerFactory.getLogger("app-log");

    /**
     * Object Mapper
     */
    private ObjectMapper objectMapper;

    /**
     * Custom validation message
     */
    private microValidatorMessage validatorMessage;

    @Autowired
    public FormService(final FormRepo repository,
                       final ObjectMapper objectMapper,
                       final microValidatorMessage validatorMessage) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.validatorMessage = validatorMessage;
    }

    private static InputStream inputStreamFromClasspath(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * Method to get forms.
     *
     * @param  tenantId Long tenantId
     * @return form template JsonNode
     * @throws IOException if there is an issue
     */
    @Transactional
    public JsonNode validateForm(
            final Long tenantId, final String domain, final String name) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        final Set<Object> errors = new HashSet();

        try (
                InputStream schemaStream = inputStreamFromClasspath("validations/productSchemaV1.json");
                InputStream jsonStream = inputStreamFromClasspath("validations/product.json");
        ) {
            JsonNode json = objectMapper.readTree(jsonStream);
            SchemaValidatorsConfig config = new SchemaValidatorsConfig();
            config.setFailFast(false);
            JsonSchema schema = schemaFactory.getSchema(schemaStream, config);

            Set<ValidationMessage> validationResult = schema.validate(json);

            // print validation errors
            if (((Set<?>) validationResult).isEmpty()) {
                log.info("Empty Errors");
            } else {
                validationResult.forEach(vm -> {
                    errors.add(validatorMessage.getMessage(vm));
                 });
            }
        }catch (JsonSchemaException e) {
            errors.add(e.getMessage());
            log.info(e.getMessage());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return objectMapper.convertValue(errors, JsonNode.class);
    }

    /**
     * Method to get forms.
     *
     * @param  tenantId Long tenantId
     * @return form template JsonNode
     * @throws IOException if there is an issue
     */
    @Transactional
    public JsonNode getForms(
            final Long tenantId, final String domain, final String name) throws IOException {

        FormProjection form = repository.geFormByName(tenantId, domain, name);

        final Set<String> fieldNames = new HashSet();
        final ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(
                form.getFields()).get("fields");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                fieldNames.add(jsonNode.textValue());
            }
        }

        final ArrayList<Map> fields = repository.getFieldsByNames(tenantId, fieldNames, domain);
        //final ArrayList<FieldProjection> fields= new ArrayList<FieldProjection>();

        final String schema = generateSchema(fields, form.getValidations());
        return objectMapper.readTree(schema);
    }

    /**
     * Method to get forms.
     *
     * @param  tenantId Long tenantId
     * @return form template JsonNode
     * @throws IOException if there is an issue
     */
    @Transactional
    public HashMap<String, JsonNode> getFields(
            final Long tenantId, final String domain, final String name) throws IOException {

        FormProjection form = repository.geFormByName(tenantId, domain, name);

        final Set<String> fieldNames = new HashSet();
        final ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(
                form.getFields()).get("fields");

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                fieldNames.add(jsonNode.textValue());
            }
        }

        final ArrayList<Map> fields = repository.getFieldMetaByNames(tenantId, fieldNames, domain);
        HashMap<String, JsonNode> fieldMap = new HashMap<>();
        fields.stream().forEach(field -> {
            try {
                fieldMap.put(field.get("code").toString(), objectMapper.readTree(field.get("meta").toString()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Fields : " + fieldMap.toString());
        return fieldMap;
    }

    /**
     * Generate json schema.
     *
     * @param  fields Long list of fields
     * @param  required String required fileds
     *
     * @return String
     */
    public String generateSchema(final ArrayList<Map> fields, final String required) {

        final StringJoiner schemaTemplate = new StringJoiner(", ");
        final StringJoiner defTemplate = new StringJoiner(", ");

        fields.stream().forEach(field -> {
            schemaTemplate.add("\"" + field.get("code") + "\":" + field.get("meta"));
            if(field.get("definition") != null &&  ! field.get("definition").equals("") &&  ! field.get("definition").equals("null")) {
                defTemplate.add("\"" + field.get("definition_name") + "\":" + field.get("definition"));
            }
        });
        final String schema = applyTemplate(schemaTemplate.toString(), defTemplate.toString(), required);

        return schema.toString();
    }


    /**
     * Generate json schema.
     *
     * @param  properties String properites
     * @param  validations String validations
     *
     * @return String
     */
    public String applyTemplate(final String properties, final String definitions,  final String validations) {
        final Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("properties", properties);
        valuesMap.put("definitions", definitions);
        valuesMap.put("required", validations);
        final String templateString = getTemplate();
        final StringSubstitutor sub = new StringSubstitutor(valuesMap);
        final String resolvedString = sub.replace(templateString);

        return resolvedString;
    }

    /**
     * Get template.
     *
     * @return String
     */
    public static String getTemplate() {
        final ResourceLoader resourceLoader = new DefaultResourceLoader();
        final Resource resource = resourceLoader.getResource("classpath:validations/productSchemaTemplate.json");

        return convertToString(resource);
    }

    /**
     * Convert to string.
     *
     * @param  resource resource
     * @return String
     */
    public static String convertToString(final Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8")) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
