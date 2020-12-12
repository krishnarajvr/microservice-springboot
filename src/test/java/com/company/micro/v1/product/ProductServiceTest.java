package com.company.micro.v1.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Optional;

import com.company.micro.common.ApplicationMode;
import com.company.micro.common.MessageByLocale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.company.micro.common.errors.ResourceNotFoundException;
import com.company.micro.entity.Product;

public class ProductServiceTest {

    @Mock
    private ProductRepo repository;

    @Mock
    private ApplicationMode appMode;

    @Mock
    private MessageByLocale messageByLocale;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFetchProductByIdWithInvalidId() {
        final ProductService service = new ProductService(repository, appMode, messageByLocale);

        Mockito.when(repository.fetchProductById(anyLong(), anyLong()))
               .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.fetchProductById(1L, 1L);
        });
    }

    @Test
    void testFetchProductByIdWithValidId() {
        final ProductService service = new ProductService(repository, appMode, messageByLocale);
        final Product expected = new Product();

        Mockito.when(repository.fetchProductById(anyLong(), anyLong()))
               .thenReturn(Optional.of(expected));

        final Product actual = service.fetchProductById(1L, 1L);

        assertEquals(expected, actual);
    }

}