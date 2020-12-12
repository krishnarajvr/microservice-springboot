package com.company.micro.config.interceptor;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.company.micro.common.RequestContext;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Populates log data for each request.
 */
@Configuration
public class LoggerInterceptor extends GlobalInterceptors {

    /**
     * Name of this application.
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * Stores request scoped data.
     */
    @Autowired
    private RequestContext requestContext;

    /**
     * Populate request specific data.
     *
     * @param request
     * @param response
     * @param handler
     * @throws JoranException joran exception
     */
    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws JoranException {
        final LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(logContext);
        //loggerContext.reset(); // override default configuration
        // inject the name of the current application dynamically based on the tenant_id
        // property of the LoggerContext
        logContext.putProperty("tenant", "tenant-" + String.valueOf(requestContext.getTenantId()));
        jc.doConfigure("src/main/resources/logback-spring.xml");

        if(isBlank(requestContext.getRequestId())) {
            requestContext.setRequestId(UUID.randomUUID().toString());
        }

        // Generate a unique id for the local log
        MDC.put("logId", requestContext.getRequestId());
        MDC.put("serviceName", applicationName);
        MDC.put("protocol", request.getProtocol());
        MDC.put("method", request.getMethod());
        MDC.put("request", getRequestPathWithParams(request));
        MDC.put("tenantId", String.valueOf(requestContext.getTenantId()));
        MDC.put("userId", String.valueOf(requestContext.getUserId()));
        MDC.put("requestId", requestContext.getRequestId());

        return true;
    }

    /**
     * Populate response specific data.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex
    ) {
        final Integer httpStatus = response.getStatus();
        MDC.put("status", String.valueOf(httpStatus));
        MDC.put("message", generateLogMessage(httpStatus));

        MDC.clear();
    }

    /**
     * Generate log message based on response status.
     *
     * @param httpStatus HTTP status
     * @return log message
     */
    private String generateLogMessage(final Integer httpStatus) {
        if (httpStatus < HttpStatus.OK.value()) {
            return "informal";
        }

        if (httpStatus >= HttpStatus.OK.value() && httpStatus < HttpStatus.MULTIPLE_CHOICES.value()) {
            return "success";
        }

        if (httpStatus >= HttpStatus.MULTIPLE_CHOICES.value() && httpStatus < HttpStatus.BAD_REQUEST.value()) {
            return "redirection";
        }

        if (httpStatus >= HttpStatus.BAD_REQUEST.value() && httpStatus < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "client error";
        }

        return "internal server error";
    }

    /**
     * Return request path with query params.
     *
     * @param request HTTP request
     * @return request path with params
     */
    private String getRequestPathWithParams(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String queryString = request.getQueryString();
        final StringBuffer requestPathWithParams = new StringBuffer();
        requestPathWithParams.append(requestURI);

        if (queryString != null) {
            requestPathWithParams.append("?").append(queryString);
        }

        return requestPathWithParams.toString();
    }

}
