package com.company.micro.v1.product;

import com.company.micro.common.APIResponse;
import com.company.micro.common.ApplicationMode;
import com.company.micro.common.MessageByLocale;
import com.company.micro.common.dto.DtoListWrapper;
import com.company.micro.common.errors.AccessDeniedException;
import com.company.micro.helper.PageHelper;
import com.company.micro.helper.SettingsHelper;
import com.company.micro.v1.product.query.filter.ProductFilter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.company.micro.common.ErrorData;
import com.company.micro.common.RequestContext;
import com.company.micro.entity.Product;
import com.company.micro.v1.product.dto.ProductDto;
import com.company.micro.v1.product.projection.ProductProjection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

/**
 * Controller for product related APIs (v1).
 */
@RestController("productControllerV1")
@RequestMapping("/v1")
public class ProductController {

    protected static final Logger defaultLog = LoggerFactory.getLogger(ProductController.class);
    protected static final Logger appLog = LoggerFactory.getLogger("app-log");
    protected static final Logger auditLog = LoggerFactory.getLogger("audit-log");
    protected static final Logger infoLog = LoggerFactory.getLogger("info-log");

    /**
     * Request scoped data (use only at the controller level).
     */
    protected RequestContext requestContext;

    /**
     * Product service.
     */
    protected ProductService service;

    /**
     * Application Mode.
     */
    protected ApplicationMode appMode;

    /**
     * Product Validator.
     */
    protected ProductValidator productValidator;

    /**
     * Message Locale.
     */
    protected MessageByLocale messageByLocale;

    /**
     * Object Mapper.
     */
    private ObjectMapper objectMapper;

    /**
     * Settings Helper.
     */
    protected SettingsHelper tenantSettings;

    @Autowired
    public ProductController(final RequestContext requestContext,
                             @Qualifier("productServiceV1") final ProductService service,
                             final ApplicationMode applicationMode,
                             final ProductValidator productValidator,
                             final MessageByLocale messageByLocale,
                             final ObjectMapper objectMapper,
                             final SettingsHelper tenantSettings
    ) {
        this.appMode = applicationMode;
        this.requestContext = requestContext;
        this.service = service;
        this.productValidator = productValidator;
        this.messageByLocale = messageByLocale;
        this.objectMapper = objectMapper;
        this.tenantSettings = tenantSettings;
    }

    /**
     * API to fetch a product by its ID.
     *
     * @param productId product ID
     * @return Product details if found
     */
    @Operation(summary = "Get a product by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product details found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})
    @GetMapping("/products/{id}")
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

        //final JSONObject settings = tenantSettings.getSettings(requestContext.getTenantId());
        //appLog.info(settings.toString());

        final Product product = service.fetchProductById(requestContext.getTenantId(), productId);

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

    /**
     * API to Save Product.
     *
     * @param productData Product data
     * @return Product details which is created
     */
    @Operation(summary = "Save Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid product supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "401", description = "Access denied",
                    content = @Content)})
    @PostMapping("/products")
    public ResponseEntity<APIResponse> saveProduct(
            @RequestBody Map<String, JsonNode> productData
    ) {
        appLog.info("{}, {}",
                keyValue("requestUrl", "/products"),
                keyValue("request", productData));

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }

        //Test mode enabled
        if (appMode.isTestMode() && appMode.mocker().isEnabled("products", "GET")) {
            return appMode.mocker().mockPost("/products",
                    objectMapper.convertValue(productData, ObjectNode.class));
        }

        final JsonNode productDto = productData.get("product");

        // dynamic product validation - throws validation exception
        productValidator.validateProductV1(productDto, requestContext.getTenantId(), requestContext.getDomain());

        final Product saveProduct = service.saveProduct(requestContext.getTenantId(), productDto);
        final APIResponse<Map<String, ProductDto>> apiResponse = new APIResponse.Builder<Map<String, ProductDto>>()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonMap("product", new ProductDto(saveProduct)))
                .traceId(requestContext.getRequestId())
                .build();

        appLog.info("{}, {}",
                keyValue("requestUrl", "/products"),
                keyValue("response", apiResponse));

        return ResponseEntity.ok(apiResponse);
    }


    /**
     * API to Save Product.
     *
     * @param productData Product data
     * @return Product details which is created
     */
    @Operation(summary = "Update Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid product supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorData.class))}),
            @ApiResponse(responseCode = "401", description = "Access denied",
                    content = @Content)})
    @PatchMapping("/products/{id}")
    public ResponseEntity<APIResponse> saveProduct(
            @PathVariable(value = "id") Long id,
            @RequestBody Map<String, JsonNode> productData
    ) {
        appLog.info("{}, {}",
                keyValue("requestUrl", "/products"),
                keyValue("request", productData));

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }

        //Test mode enabled
        if (appMode.isTestMode() && appMode.mocker().isEnabled("products", "GET")) {
            return appMode.mocker().mockPost("/products",
                    objectMapper.convertValue(productData, ObjectNode.class));
        }

        final JsonNode productDto = productData.get("product");

        // dynamic product validation - throws validation exception
        productValidator.validateProductAPI(productDto, requestContext.getTenantId(), requestContext.getDomain());

        final Product saveProduct = service.updateProduct(id, requestContext.getTenantId(), productDto);
        final APIResponse<Map<String, ProductDto>> apiResponse = new APIResponse.Builder<Map<String, ProductDto>>()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonMap("product", new ProductDto(saveProduct)))
                .traceId(requestContext.getRequestId())
                .build();

        appLog.info("{}, {}",
                keyValue("requestUrl", "/products"),
                keyValue("response", apiResponse));

        return ResponseEntity.ok(apiResponse);
    }



    /**
     * Get  products with filters and pagination
     *
     * @param productFilter Filter for product
     * @param pageable      page of AdminProductProjection
     * @return API Response
     */
    @GetMapping("/products")
    public ResponseEntity<APIResponse> getProducts(
            final ProductFilter productFilter,
            final Pageable pageable) {

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }

        final Page<ProductProjection> productPage = service.getProducts(
                requestContext.getTenantId(),
                productFilter.generateBooleanBuilder(),
                pageable);


        final DtoListWrapper<ProductDto> responseData = new DtoListWrapper<>(
                "products",
                productPage.getContent()
                        .stream()
                        .map(ProductDto::new)
                        .collect(Collectors.toList()),
                PageHelper.createPaginationMeta(productPage)
        );

        final APIResponse<DtoListWrapper<ProductDto>> apiResponse = new APIResponse.Builder<DtoListWrapper<ProductDto>>()
                .status(HttpStatus.OK.value())
                .data(responseData)
                .build();

        appLog.info("{}, {}",
                keyValue("requestUrl", "/products"),
                keyValue("response", apiResponse));

        return ResponseEntity.ok(apiResponse);
    }

}
