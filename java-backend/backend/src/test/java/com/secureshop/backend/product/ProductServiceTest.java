package com.secureshop.backend.product;

import com.secureshop.backend.category.Category;
import com.secureshop.backend.category.CategoryRepository;
import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.CategoryNotFoundException;
import com.secureshop.backend.exception.ProductNotFoundException;
import com.secureshop.backend.mapper.ProductMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;

    private Category category;
    private Product product;
    private ProductRequestDTO request;
    private ProductResponseDTO response;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Products")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming Laptop")
                .price(85000.0)
                .category(category)
                .build();

        request = new ProductRequestDTO(
                "Laptop",
                "Gaming Laptop",
                85000.0,
                1L
        );

        response = new ProductResponseDTO(
                1L,
                "Laptop",
                "Gaming Laptop",
                85000.0,
                1L,
                "Electronics"
        );
    }

    @Test
    @DisplayName("getAllProducts")
    void getAllProducts_shouldReturnPagedResponse() {

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> page =
                new PageImpl<>(List.of(product));

        when(repository.findAll(pageable))
                .thenReturn(page);

        when(mapper.toResponse(product))
                .thenReturn(response);

        PagedResponse<ProductResponseDTO> result =
                service.getAllProducts(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Laptop",
                result.getContent().getFirst().getName());

        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("searchProducts")
    void searchProducts_shouldReturnProducts() {

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> page =
                new PageImpl<>(List.of(product));

        when(repository.findByNameContainingIgnoreCase(
                "Laptop",
                pageable))
                .thenReturn(page);

        when(mapper.toResponse(product))
                .thenReturn(response);

        PagedResponse<ProductResponseDTO> result =
                service.searchProducts(
                        "Laptop",
                        pageable);

        assertEquals(1,
                result.getContent().size());

        verify(repository)
                .findByNameContainingIgnoreCase(
                        "Laptop",
                        pageable);
    }

    @Test
    @DisplayName("getProductById")
    void getProductById_shouldReturnProduct() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        when(mapper.toResponse(product))
                .thenReturn(response);

        ProductResponseDTO result =
                service.getProductById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());

        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("getProductById product not found")
    void getProductById_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.getProductById(100L));

        verify(repository).findById(100L);
    }

    @Test
    @DisplayName("createProduct")
    void createProduct_shouldSaveProduct() {

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(mapper.toEntity(request))
                .thenReturn(product);

        when(repository.save(any(Product.class)))
                .thenReturn(product);

        when(mapper.toResponse(product))
                .thenReturn(response);

        ProductResponseDTO result =
                service.createProduct(request);

        assertEquals("Laptop",
                result.getName());

        ArgumentCaptor<Product> captor =
                ArgumentCaptor.forClass(Product.class);

        verify(repository)
                .save(captor.capture());

        assertEquals(
                category.getId(),
                captor.getValue()
                        .getCategory()
                        .getId());
    }

    @Test
    @DisplayName("createProduct category not found")
    void createProduct_shouldThrowCategoryNotFound() {

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> service.createProduct(request));

        verify(repository, never())
                .save(any());
    }
        @Test
    @DisplayName("updateProduct")
    void updateProduct_shouldUpdateProduct() {

        ProductRequestDTO updateRequest =
                new ProductRequestDTO(
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0,
                        1L
                );

        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Laptop Pro")
                .description("Gaming Laptop RTX")
                .price(99000.0)
                .category(category)
                .build();

        ProductResponseDTO updatedResponse =
                new ProductResponseDTO(
                        1L,
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0,
                        1L,
                        "Electronics"
                );

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(repository.save(any(Product.class)))
                .thenReturn(updatedProduct);

        when(mapper.toResponse(updatedProduct))
                .thenReturn(updatedResponse);

        ProductResponseDTO result =
                service.updateProduct(1L, updateRequest);

        assertEquals("Laptop Pro", result.getName());
        assertEquals(99000.0, result.getPrice());

        verify(repository).save(product);
    }

    @Test
    @DisplayName("updateProduct product not found")
    void updateProduct_shouldThrowProductNotFound() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.updateProduct(100L, request));

        verify(repository).findById(100L);
    }

    @Test
    @DisplayName("updateProduct category not found")
    void updateProduct_shouldThrowCategoryNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> service.updateProduct(1L, request));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deleteProduct")
    void deleteProduct_shouldDeleteProduct() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        doNothing()
                .when(repository)
                .delete(product);

        service.deleteProduct(1L);

        verify(repository).delete(product);
    }

    @Test
    @DisplayName("deleteProduct product not found")
    void deleteProduct_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.deleteProduct(100L));

        verify(repository, never())
                .delete(any());
    }

    @Test
    @DisplayName("getProductsByCategory")
    void getProductsByCategory_shouldReturnPagedProducts() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> page =
                new PageImpl<>(List.of(product));

        when(repository.findByCategoryId(
                1L,
                pageable))
                .thenReturn(page);

        when(mapper.toResponse(product))
                .thenReturn(response);

        PagedResponse<ProductResponseDTO> result =
                service.getProductsByCategory(
                        1L,
                        pageable);

        assertEquals(
                1,
                result.getContent().size());

        assertEquals(
                "Laptop",
                result.getContent().getFirst().getName());

        verify(repository)
                .findByCategoryId(
                        1L,
                        pageable);
    }
}