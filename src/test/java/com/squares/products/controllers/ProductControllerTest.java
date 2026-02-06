package com.squares.products.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squares.products.exceptions.GlobalExceptionHandler;
import com.squares.products.models.ProductModel;
import com.squares.products.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private com.squares.products.mappers.ProductMapper productMapper;

    @MockBean
    private com.squares.products.mappers.ErrorMapper errorMapper;

    @Test
    void createsProduct() throws Exception {
        ProductModel saved = new ProductModel();
        saved.setId("abc");
        saved.setName("Caneta");
        saved.setValue(2.5f);

        when(productRepository.save(any(ProductModel.class))).thenReturn(saved);
        when(productMapper.toResponse(any(ProductModel.class)))
                .thenReturn(new com.squares.products.dtos.ProductResponseDTO(
                        "abc",
                        "SKU-001",
                        "Caneta",
                        "Caneta azul",
                        "Papelaria",
                        "Bic",
                        "un",
                        10,
                        2,
                        1.0f,
                        2.5f,
                        2.5f,
                        "BRL",
                        true,
                        null,
                        null,
                        "https://example.com/caneta.png",
                        "7891234567890"
                ));

        String body = objectMapper.writeValueAsString(new ProductInput(
                "SKU-001",
                "Caneta",
                "Caneta azul",
                "Papelaria",
                "Bic",
                "un",
                10,
                2,
                1.0f,
                2.5f,
                2.5f,
                "BRL",
                true,
                "https://example.com/caneta.png",
                "7891234567890"
        ));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc"));
    }

    @Test
    void validatesProductValue() throws Exception {
        String body = objectMapper.writeValueAsString(new ProductInput(
                "SKU-002",
                "Caneta",
                "Caneta azul",
                "Papelaria",
                "Bic",
                "un",
                10,
                2,
                1.0f,
                2.5f,
                0.0f,
                "BRL",
                true,
                "https://example.com/caneta.png",
                "7891234567890"
        ));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.value").exists());
    }

    @Test
    void returnsPagedProducts() throws Exception {
        ProductModel model = new ProductModel();
        model.setId("1");
        model.setName("Caderno");
        model.setValue(12.5f);

        Page<ProductModel> page = new PageImpl<>(List.of(model), PageRequest.of(0, 1), 1);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(productMapper.toResponse(any(ProductModel.class)))
                .thenReturn(new com.squares.products.dtos.ProductResponseDTO(
                        "1",
                        "SKU-001",
                        "Caderno",
                        null,
                        null,
                        null,
                        null,
                        0,
                        0,
                        null,
                        null,
                        12.5f,
                        "BRL",
                        true,
                        null,
                        null,
                        null,
                        null
                ));

        mockMvc.perform(get("/products?page=0&size=1"))
                .andExpect(status().isOk());
    }

    @Test
    void returnsNotFoundWhenMissing() throws Exception {
        when(productRepository.findById(eq("missing"))).thenReturn(Optional.empty());
        when(errorMapper.toResponse(any(com.squares.products.exceptions.ApiError.class)))
                .thenReturn(new com.squares.products.dtos.ErrorResponseDTO(
                        null,
                        404,
                        "Not Found",
                        "Product not found.",
                        "/products/missing",
                        null
                ));

        mockMvc.perform(get("/products/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found."));
    }

    private record ProductInput(
            String sku,
            String name,
            String description,
            String category,
            String brand,
            String unit,
            Integer stockQuantity,
            Integer minStock,
            Float costPrice,
            Float salePrice,
            Float value,
            String currency,
            Boolean active,
            String imageUrl,
            String barcode
    ) {
    }
}
