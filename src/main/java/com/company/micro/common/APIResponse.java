package com.company.micro.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class represents the general response structure of the api.
 * @param <T> response data
 */
@NoArgsConstructor
@Getter
public final class APIResponse<T> implements Serializable {

    /**
     * HTTP status.
     */
    private Integer status;

    /**
     * Error data.
     */
    private ErrorData error;

    /**
     * Response data.
     */
    private T data;

    /**
     * Trace Id.
     */
    private String traceId;


    private APIResponse(final Builder<T> builder) {
        this.status = builder.status;
        this.error = builder.error;
        this.data = builder.data;
        this.traceId = builder.traceId;
    }

    /**
     * Builder for {@link APIResponse}.
     * @param <T> response data type
     */
    public static final class Builder<T> {

        /**
         * Status value.
         */
        private Integer status;

        /**
         * Error value.
         */
        private ErrorData error;

        /**
         * Data value.
         */
        private T data;


        /**
         * Trace Id.
         */
        private String traceId;

        /**
         * Set HTTP status.
         * @param statusValue value for status
         * @return builder
         */
        public Builder<T> status(final Integer statusValue) {
            this.status = statusValue;

            return this;
        }

        /**
         * Set error data.
         * @param errorValue value for error
         * @return builder
         */
        public Builder<T> error(final ErrorData errorValue) {
            this.error = errorValue;

            return this;
        }

        /**
         * Set response data.
         * @param dataValue value for data
         * @return builder
         */
        public Builder<T> data(final T dataValue) {
            this.data = dataValue;

            return this;
        }

        /**
         * Set trace Id.
         * @param traceId log trace Id
         * @return builder
         */
        public Builder<T> traceId(final String traceId) {
            this.traceId = traceId;

            return this;
        }

        /**
         * @return instance of {@link APIResponse}.
         */
        public APIResponse<T> build() {
            return new APIResponse<>(this);
        }
    }
}
