package com.company.micro.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the structure of error data in api response.
 */
@Getter
@JsonDeserialize(as = ErrorData.class)
public final class ErrorData implements Serializable {

    /**
     * Error code.
     */
    private String code;

    /**
     * Error target.
     */
    private String target;

    /**
     * Error path.
     */
    private String path;

    /**
     * Key path.
     */
    private HashMap<String,String> key;

    /**
     * Error message.
     */
    private String message;

    /**
     * Nested errors.
     */
    private List<ErrorData> details;

    private ErrorData(final Builder builder) {
        this.code = builder.code;
        this.target = builder.target;
        this.message = builder.message;
        this.details = builder.details;
        this.key = builder.key;
        this.path = builder.path;
    }

    private ErrorData(String code, String target, String message, List<ErrorData> details) {
        this.code = code;
        this.target = target;
        this.message = message;
        this.details = details;
    }

    private ErrorData() {
        this.code = "";
        this.target = "";
        this.message = "";
        this.path = "";
        this.details = null;
    }

    /**
     * Builder for {@link ErrorData}.
     */
    public static final class Builder {

        /**
         * Code value.
         */
        private String code;

        /**
         * Target value.
         */
        private String target;

        /**
         * Path value.
         */
        private String path;

        /**
         * Key value.
         */
        private HashMap<String,String> key;

        /**
         * Message value.
         */
        private String message;

        /**
         * Details value.
         */
        private List<ErrorData> details;

        /**
         * Set error code.
         *
         * @param codeValue value for code
         * @return builder
         */
        public Builder code(final String codeValue) {
            this.code = codeValue;

            return this;
        }

        /**
         * Set error target.
         *
         * @param targetValue value for target
         * @return builder
         */
        public Builder target(final String targetValue) {
            this.target = targetValue;

            return this;
        }

        /**
         * Set key.
         *
         * @param key value of data
         * @return builder
         */
        public Builder key(final HashMap<String,String> key) {
            this.key = key;

            return this;
        }

        /**
         * Set error target.
         *
         * @param pathValue value for path
         * @return builder
         */
        public Builder path(final String pathValue) {
            this.path = pathValue;

            return this;
        }

        /**
         * Set error message.
         *
         * @param messageValue value for message
         * @return builder
         */
        public Builder message(final String messageValue) {
            this.message = messageValue;

            return this;
        }

        /**
         * Set nested errors.
         *
         * @param detailsValue value for details
         * @return builder
         */
        public Builder details(final List<ErrorData> detailsValue) {
            this.details = detailsValue;

            return this;
        }

        /**
         * @return instance of {@link ErrorData}.
         */
        public ErrorData build() {
            return new ErrorData(this);
        }

    }

}
