package com.company.micro.config.interceptor;

import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.micro.v1.product.ProductController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.company.micro.common.RequestContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * Perform actions before handling, after handling or after completion (when the view is rendered)
 * of a request.
 */
@Configuration
public class ClientRequestInterceptor extends GlobalInterceptors {

    protected static final Logger Log = LoggerFactory.getLogger(ProductController.class);

    /**
     * Context to store request scoped data.
     */
    private RequestContext requestContext;

    @Autowired
    public ClientRequestInterceptor(final RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Populates {@link RequestContext} with request scoped data.
     *
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        Log.info("Pre Request - " + request.getRequestURI());

        final String requestIdHeader = request.getHeader("X-Request-Id");
        final String tenantIdHeader = request.getHeader("tenantId");
        final String domaindHeader = request.getHeader("domain");
        final String userIdHeader = request.getHeader("userId");
        final String jwtTokenHeader = request.getHeader("X-Jwt-Token");

        requestContext.setRequestId(isBlank(requestIdHeader) ? UUID.randomUUID().toString() : requestIdHeader);
        requestContext.setTenantId(isBlank(tenantIdHeader) ? null : Long.valueOf(tenantIdHeader));
        requestContext.setDomain(isBlank(domaindHeader) ? "CORE" : domaindHeader);
        requestContext.setUserId(isBlank(userIdHeader) ? null : Long.valueOf(userIdHeader));
        requestContext.setJwtToken(isBlank(jwtTokenHeader) ? null : jwtTokenHeader);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        Log.info("postHandle : " + response.getStatus());

    }
    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object
                    handler, Exception exception) throws Exception {

        Log.info("afterCompletion : " + response.getStatus());
    }

}
