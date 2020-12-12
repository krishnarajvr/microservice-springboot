package com.company.micro.helper;

import com.company.micro.v1.form.FormRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.company.micro.v1.form.projection.FormProjection;
import org.apache.commons.text.StringSubstitutor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Service for Common User related APIs (v1).
 */
@Component
public class FormHelper {

    /**
     * Repository for Common User Operations.
     */
    private FormRepo repository;


    /**
     * Request scoped data.
     */
    private ObjectMapper objectMapper;

    @Autowired
    public FormHelper(final FormRepo repository,
                      final ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Method to get forms.
     *
     * @param  tenantId Long tenantId
     * @return form template JsonNode
     * @throws IOException if there is an issue
     */
    @Transactional
    public JSONObject getForms(
            final Long tenantId, final String domain, final String name) {

        FormProjection form = repository.geFormByName(tenantId, domain, name);

        final Set<String> fieldNames = new HashSet();

        final ArrayNode arrayNode;

        try {
            arrayNode = (ArrayNode) objectMapper.readTree(
                    form.getFields()).get("fields");
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                fieldNames.add(jsonNode.textValue());
            }
        }

        final ArrayList<Map> fields = repository.getFieldsByNames(tenantId, fieldNames, domain);
        //final ArrayList<FieldProjection> fields= new ArrayList<FieldProjection>();

        final String schema = generateSchema(fields, form.getValidations());
        final JSONObject jsonSchema;
        jsonSchema =  objectMapper.convertValue(schema, JSONObject.class);

        return  jsonSchema;
    }


    /**
     * Method to get forms.
     *
     * @param  tenantId Long tenantId
     * @return form template JsonNode
     * @throws IOException if there is an issue
     */
    @Transactional
    public String getFormString(
            final Long tenantId, final String domain, final String name) {

        FormProjection form = repository.geFormByName(tenantId, domain, name);

        final Set<String> fieldNames = new HashSet();

        final ArrayNode arrayNode;

        try {
            arrayNode = (ArrayNode) objectMapper.readTree(
                    form.getFields()).get("fields");
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }

        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                fieldNames.add(jsonNode.textValue());
            }
        }

        final ArrayList<Map> fields = repository.getFieldsByNames(tenantId, fieldNames, domain);
        //final ArrayList<FieldProjection> fields= new ArrayList<FieldProjection>();

        return  generateSchema(fields, form.getValidations());
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
