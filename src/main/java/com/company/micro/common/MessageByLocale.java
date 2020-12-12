package com.company.micro.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * <h1>Message By Locale Utility</h1>
 * Get response messages for all supported languages based on
 * the locale information in each request.
 */
@Component
public class MessageByLocale {

    /**
     * Source for locale specific messages.
     */
    private MessageSource messageSource;

    public MessageByLocale(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get Messages.
     *
     * @param id message properties id.
     * @return message by id
     */
    public String getMessage(final String id) {
        final Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(id, null, id, locale);
    }

    /**
     * Get messages.
     *
     * @param id String id
     * @param args Object of args
     * @return String messags
     */
    public String getMessage(final String id, final Object[] args) {
        final Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(id, args, id, locale);
    }
}
