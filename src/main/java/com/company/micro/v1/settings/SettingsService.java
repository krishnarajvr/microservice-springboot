package com.company.micro.v1.settings;

import com.company.micro.repository.SettingsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.micro.entity.Settings;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

/**
 * Service for Common User related APIs (v1).
 */
@org.springframework.stereotype.Service("SettingsServiceV1")
public class SettingsService {

    /**
     * Repository for Common User Operations.
     */
    private SettingsRepository repository;
    /**
     * Request scoped data.
     */
    private ObjectMapper objectMapper;

    @Autowired
    public SettingsService(final SettingsRepository repository,
                           final ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Method to Get common users of an institution.
     *
     * @param tenantId tenantId
     * @return Settings Tenant Settings
     * @throws IOException IOExcepton
     */
    @Transactional
    public JsonNode getSettings(
            final Long tenantId) throws IOException {

        final Settings systemSettings = repository.findOneByTenantIdAndType(0L, "SYSTEM");
        final Settings tenantSettings = repository.findOneByTenantId(tenantId);
        final Settings domainSettings = Optional.of(tenantSettings)
                .filter(settings -> settings.getType().equals("HEALTH"))
                .map(settings -> repository.findOneByTenantIdAndType(0L, "HEALTH"))
                .orElseGet(() -> repository.findOneByTenantIdAndType(0L, "SCHOLAR"));

        JSONObject finalSettings = mergeJSONObjects(new JSONObject(systemSettings.getSettings()),
                new JSONObject(domainSettings.getSettings()));
        finalSettings = mergeJSONObjects(finalSettings,
                new JSONObject(tenantSettings.getSettings()));

        return objectMapper.readTree(finalSettings.toString());
    }

    /**
     * Merge Json objects.
     *
     * @param json1 JSONObject first json object
     * @param json2 JSONObject second json object
     * @return JSONObject json ojbect
     */
    public static JSONObject mergeJSONObjects(final JSONObject json1, final JSONObject json2) {
        JSONObject mergedJSON = new JSONObject();
        try {
            mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
            for (String crunchifyKey : JSONObject.getNames(json2)) {
                mergedJSON.put(crunchifyKey, json2.get(crunchifyKey));
            }

        } catch (JSONException e) {
            throw new RuntimeException("JSON Exception" + e);
        }
        return mergedJSON;
    }

}
