package com.company.micro.common;

import com.company.micro.entity.Language;
import com.company.micro.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component("messageSource")
public class DbMessageSource extends AbstractMessageSource {

    @Autowired
    protected LanguageRepository languageRepository;

    @Autowired
    protected RequestContext requestContext;

    private static final String DEFAULT_LOCALE_CODE = "en";

    @Override
    protected MessageFormat resolveCode(String key, Locale locale) {
        Long tenantId = requestContext.getTenantId();
        Language message = languageRepository.findByTenantIdAndMessageKeyAndLocale(tenantId, key, locale.getLanguage());
        if (message == null) {
            message = languageRepository.findByTenantIdAndMessageKeyAndLocale(0L, key, DEFAULT_LOCALE_CODE);
        }

        if (message == null) {
            message = new Language();
            message.setMessage("");
        }
        return new MessageFormat(message.getMessage(), locale);
    }

}