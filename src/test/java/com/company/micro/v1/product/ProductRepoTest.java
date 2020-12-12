package com.company.micro.v1.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.company.micro.entity.Product;
import com.company.micro.repository.ProductRepository;

public class ProductRepoTest {

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Tag("Issue-100")
    void testFetchProductByIdWithValidId() {
        final ProductRepo repository = new ProductRepo(productRepository);

        Mockito.when(productRepository.findByTenantIdAndId(anyLong(), anyLong()))
               .thenReturn(Optional.of(new Product()));

        final Optional<Product> product = repository.fetchProductById(1L, 1L);

        assertTrue(product.isPresent());
    }

    @Test
    void testFetchProductByIdWithInvalidId() {
        final ProductRepo repository = new ProductRepo(productRepository);

        Mockito.when(productRepository.findByTenantIdAndId(anyLong(), anyLong()))
               .thenReturn(Optional.empty());

        final Optional<Product> product = repository.fetchProductById(1L, 1L);

        assertFalse(product.isPresent());
    }

}