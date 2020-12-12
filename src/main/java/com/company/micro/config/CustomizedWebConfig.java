package com.company.micro.config;

import com.company.micro.common.RequestContext;
import com.company.micro.config.interceptor.GlobalInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

/**
 * Contains MVC configurations like interceptors, content negotiation, general parameter settings etc.
 */
@Configuration
public class CustomizedWebConfig implements WebMvcConfigurer {

    /**
     * Stores request scoped data.
     */
    private RequestContext requestContext;

    /**
     * List of all global interceptors.
     */
    private List<GlobalInterceptors> globalInterceptors;

    @Autowired
    public CustomizedWebConfig(
            final RequestContext requestContext,
            final List<GlobalInterceptors> globalInterceptors
    ) {
        this.requestContext = requestContext;
        this.globalInterceptors = globalInterceptors;
    }

    /**
     * Add interceptors to the registry.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        this.globalInterceptors.forEach(registry::addInterceptor);
    }

    /**
     * Content negotiation settings.
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Rest template.
     *
     * @param builder Rest template builder
     * @return restTemplate RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        final RestTemplate restTemplate = builder.build();
        restTemplate.setErrorHandler(new CustomRestTemplateResponseErrorHandler());

        return restTemplate;
    }

    /**
     * Use this class as error handler for the restTemplate bean so that exceptions are
     * not thrown for http statuses other than 2XX.
     */
    private class CustomRestTemplateResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(final ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(final ClientHttpResponse response) throws IOException {

        }
    }
}
