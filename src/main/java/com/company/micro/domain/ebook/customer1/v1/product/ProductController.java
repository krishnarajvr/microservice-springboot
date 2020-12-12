package com.company.micro.domain.ebook.customer1.v1.product;

import com.company.micro.domain.ebook.v1.product.ProductService;
import com.company.micro.domain.ebook.customer1.v1.product.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.micro.common.APIResponse;
import com.company.micro.common.ApplicationMode;
import com.company.micro.common.MessageByLocale;
import com.company.micro.common.RequestContext;
import com.company.micro.common.errors.AccessDeniedException;
import com.company.micro.entity.Product;
import com.company.micro.helper.SettingsHelper;
import com.company.micro.v1.product.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@RestController("Wiley_Scholar_ProductController_V1")
@RequestMapping("/wiley/scholar/v1")
public class ProductController extends com.company.micro.domain.ebook.v1.product.ProductController {
    /**
     * Product service.
     */
    protected ProductService service;

    @Autowired
    public ProductController(RequestContext requestContext,
                             @Qualifier("Wiley_Scholar_ProductService_V1") ProductService service,
                             ApplicationMode applicationMode,
                             ProductValidator productValidator,
                             MessageByLocale messageByLocale,
                             ObjectMapper objectMapper,
                             SettingsHelper tenantSettings) {
        super(requestContext, service, applicationMode, productValidator, messageByLocale, objectMapper, tenantSettings);
        this.service = service;
    }


    public ResponseEntity<APIResponse> fetchProductById(
            @PathVariable(value = "id") final Long productId
    ) {
        defaultLog.info("Default:Calling products/" + productId);
        infoLog.info("Info:Calling products/" + productId);
        auditLog.info("{}, {}",
                keyValue("requestUrl", "/products" + productId),
                keyValue("product", productId));
        appLog.info("{}, {}",
                keyValue("requestUrl", "/products" + productId),
                keyValue("product", productId));

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }

        //Test mode enabled
        if (appMode.isTestMode() && appMode.mocker().isEnabled("products", "GET")) {
            return appMode.mocker().mockGet("/products/" + productId);
        }

        final Product product = service.fetchProductByIdCustom(requestContext.getTenantId(), productId);

        final APIResponse<Map<String, ProductDto>> apiResponse = new APIResponse.Builder<Map<String, ProductDto>>()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonMap("product", new ProductDto(product)))
                .traceId(requestContext.getRequestId())
                .build();

        appLog.info("{}, {}",
                keyValue("requestUrl", "/products"),
                keyValue("response", apiResponse));

        return ResponseEntity.ok(apiResponse);
    }

}
