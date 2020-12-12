

package com.company.micro.common.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

/**
 * Wrapper for list of DTOs.
 *
 * @param <T> DTO type
 */
@Getter @Setter
@JsonSerialize(using = DtoListWrapper.Serializer.class)
public class DtoListWrapper<T> {

    /**
     * key for the list.
     */
    private String key;

    /**
     * List of DTO objects.
     */
    private List<T> list;

    /**
     * Pagination details.
     */
    private PaginationMetaDTO pagination;

    public DtoListWrapper(final String key, final List<T> list, final PaginationMetaDTO pagination) {
        this.key = key;
        this.list = list;
        this.pagination = pagination;
    }

    /**
     * Serializer for the DTO list wrapper.
     */
    public static class Serializer extends JsonSerializer<Object> {

        private static final String paginationKey = "_pagination";

        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            final DtoListWrapper<?> wrapper = (DtoListWrapper<?>) value;
            jgen.writeStartObject();
            jgen.writeArrayFieldStart(wrapper.getKey());

            for (final Object o : wrapper.getList()) {
                jgen.writeObject(o);
            }

            jgen.writeEndArray();
            jgen.writeObjectField(paginationKey, wrapper.getPagination());
            jgen.writeEndObject();
        }
    }
}
