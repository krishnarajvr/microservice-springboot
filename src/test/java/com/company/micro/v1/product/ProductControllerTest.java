package com.company.micro.v1.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.micro.common.ApplicationMode;
import com.company.micro.common.MessageByLocale;
import com.company.micro.helper.SettingsHelper;
import com.company.micro.v1.product.dto.ProductDto;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.company.micro.common.APIResponse;
import com.company.micro.common.RequestContext;
import com.company.micro.common.errors.AccessDeniedException;
import com.company.micro.common.errors.ResourceNotFoundException;
import com.company.micro.entity.Product;



class ProductControllerTest {

    @Mock
    private RequestContext requestContext;

    @Mock
    private ProductService service;

    @Mock
    private ApplicationMode appMode;

    @Mock
    private ProductValidator productValidator;

    @Mock
    private MessageByLocale messageByLocale;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SettingsHelper settingsHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFetchProductByIdWithNoPublicToken() {
        final ProductController controller = new ProductController(
                requestContext,
                service,
                appMode,
                productValidator,
                messageByLocale,
                objectMapper,
                settingsHelper
        );

        Mockito.when(requestContext.hasPublicToken())
               .thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> {
            controller.fetchProductById(2L);
        });
    }

    @Test
    @Tag("Issue-100")
    void testFetchProductByIdWithInvalidId() {
        final ProductController controller = new ProductController(
                requestContext,
                service,
                appMode,
                productValidator,
                messageByLocale,
                objectMapper,
                settingsHelper
        );
        final Long tenantId = 5L;

        Mockito.when(requestContext.getTenantId())
                .thenReturn(tenantId);
        Mockito.when(requestContext.hasPublicToken())
               .thenReturn(true);
        Mockito.when(settingsHelper.getSettings(tenantId)).thenReturn(new JSONObject());

        Mockito.when(service.fetchProductById(eq(tenantId), anyLong()))
               .thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            controller.fetchProductById(2L);
        });
    }

    @Test
    void testFetchProductByIdWithValidId() {
        final ProductController controller = new ProductController(
                requestContext,
                service,
                appMode,
                productValidator,
                messageByLocale,
                objectMapper,
                settingsHelper
        );
        final Long tenantId = 2L;
        final Product product = createNewProduct(1L, tenantId, "test product");

        Mockito.when(requestContext.hasPublicToken())
               .thenReturn(true);
        Mockito.when(requestContext.getTenantId())
               .thenReturn(tenantId);
        Mockito.when(service.fetchProductById(eq(tenantId), anyLong()))
               .thenReturn(product);
        Mockito.when(settingsHelper.getSettings(tenantId)).thenReturn(new JSONObject());

        final Map<String, ProductDto> productData = Collections.singletonMap("product", new ProductDto(product));
        final APIResponse<Map<String, ProductDto>>
                responseData = new APIResponse.Builder<Map<String, ProductDto>>()
                .status(HttpStatus.OK.value())
                .data(productData)
                .build();

        final ResponseEntity<APIResponse<Map<String, ProductDto>>> expected = ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);

        final ResponseEntity<APIResponse> actual = controller.fetchProductById(1L);

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertNull(actual.getBody().getError());
        assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
        //assertEquals(product.getId(), actual.getBody().getData().get("product").getId());
        //assertEquals(product.getName(), actual.getBody().getData().get("product").getName());
    }

    private Product createNewProduct(final Long id, final Long tenantId, final String name) {
        final LocalDateTime now = LocalDateTime.now();
        final Product product = new Product();

        product.setId(id);
        product.setName(name);
        product.setCode(name);
        product.setPrice(BigDecimal.valueOf(100));
        product.setTenantId(tenantId);
        product.setCreatedTime(now);
        product.setUpdatedTime(now);

        return product;
    }
}

