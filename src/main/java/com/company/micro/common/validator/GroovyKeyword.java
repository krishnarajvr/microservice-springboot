package com.company.micro.common.validator;

import com.company.micro.common.validator.schema.JsonSchemaException;
import com.fasterxml.jackson.databind.JsonNode;
import com.company.micro.common.validator.schema.AbstractJsonValidator;
import com.company.micro.common.validator.schema.AbstractKeyword;
import com.company.micro.common.validator.schema.JsonSchema;
import com.company.micro.common.validator.schema.ValidationContext;
import com.company.micro.common.validator.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;

public class GroovyKeyword extends AbstractKeyword {
    private static final Logger logger = LoggerFactory.getLogger(GroovyKeyword.class);

    public GroovyKeyword() {
        super("groovy");
    }

    @Override
    public AbstractJsonValidator newValidator(String schemaPath, JsonNode schemaNode, JsonSchema parentSchema, ValidationContext validationContext) throws JsonSchemaException, Exception {
        // you can read validator config here
        String config = schemaNode.asText();
        return new AbstractJsonValidator(this.getValue()) {
            @Override
            public Set<ValidationMessage> validate(JsonNode node, JsonNode rootNode, String at) {
                // you can do validate here
                logger.info("config:{} path:{} node:{}", config, at, node);

                return Collections.emptySet();
            }
        };
    }
}
