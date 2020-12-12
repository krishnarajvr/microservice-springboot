package com.company.micro.v1.form;

import com.company.micro.repository.FieldRepository;
import com.company.micro.repository.FormRepository;
import com.company.micro.v1.form.projection.FormProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Form repo.
 */
@Repository("FormRepoV1")
public class FormRepo {
    /**
     * Repository to provide locale files.
     */
    private FormRepository formRepository;

    /**
     * Repository to provide themes.
     */
    private FieldRepository fieldRepository;

    @Autowired
    public FormRepo(final FormRepository formRepository, final FieldRepository fieldRepository) {
        this.formRepository = formRepository;
        this.fieldRepository = fieldRepository;
    }

    /**
     * @param tenantId tenant Id
     * @param type type
     * @return Forms forms
     */
    public FormProjection geFormByName(final Long tenantId, final String type, String name) {
        return formRepository.getFormByName(tenantId, type, name);
    }

    /**
     * @param fields set of fields
     * @return List List of fields
     */
    public ArrayList<Map> getFieldsByNames(final Long tenantId, final Set<String> fields, final String domain) {
        return fieldRepository.getFieldsByNames(tenantId, fields, domain);
    }

    /**
     * @param fields set of fields
     * @return List List of fields
     */
    public ArrayList<Map> getFieldMetaByNames(final Long tenantId, final Set<String> fields, final String domain) {
        return fieldRepository.getFieldMetaByNames(tenantId, fields, domain);
    }

}
