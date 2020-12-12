

package com.company.micro.common.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.company.micro.common.RequestContext;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Custom serializer for {@link DateTime}
 */
public class DateTimeCustomSerializer extends JsonSerializer<DateTime> {

    @Autowired
    RequestContext requestMeta;

    @Override
    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
        gen.writeString(formatter.print(value));
    }

}
