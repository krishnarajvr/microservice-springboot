package com.company.micro.common.validator;

import com.company.micro.common.validator.schema.Format;
import com.company.micro.common.validator.schema.JsonMetaSchema;
import com.company.micro.common.validator.schema.JsonSchemaFactory;
import com.company.micro.common.validator.schema.NonValidationKeyword;
import com.company.micro.common.validator.schema.SpecVersion;
import com.company.micro.common.validator.schema.ValidatorTypeCode;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class microSchemaFactory {

    @Bean
    public JsonSchemaFactory getJsonSchemaFactory() {
        // base on JsonMetaSchema.V201909 copy code below
        String URI = "https://json-schema.org/draft/2019-09/schema";
        String ID = "$id";
        List<Format> BUILTIN_FORMATS = new ArrayList<Format>(JsonMetaSchema.COMMON_BUILTIN_FORMATS);

        JsonMetaSchema zhylJsonMetaSchema = new JsonMetaSchema.Builder(URI)
                .idKeyword(ID)
                .addFormats(BUILTIN_FORMATS)
                .addKeywords(ValidatorTypeCode.getNonFormatKeywords(SpecVersion.VersionFlag.V201909))
                // keywords that may validly exist, but have no validation aspect to them
                .addKeywords(Arrays.asList(
                        new NonValidationKeyword("$schema"),
                        new NonValidationKeyword("$id"),
                        new NonValidationKeyword("title"),
                        new NonValidationKeyword("description"),
                        new NonValidationKeyword("default"),
                        new NonValidationKeyword("definitions"),
                        new NonValidationKeyword("$defs")  // newly added in 2018-09 release.
                ))
                // add your custom keyword
                .addKeyword(new GroovyKeyword())
                .build();

        return new JsonSchemaFactory.Builder().defaultMetaSchemaURI(zhylJsonMetaSchema.getUri())
                .addMetaSchema(zhylJsonMetaSchema)
                .build();
    }
}
