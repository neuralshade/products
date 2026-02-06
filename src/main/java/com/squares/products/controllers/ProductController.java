package com.squares.products.controllers;

import com.squares.products.dtos.ProductRecordDTO;
import com.squares.products.dtos.ProductResponseDTO;
import com.squares.products.exceptions.ProductNotFoundException;
import com.squares.products.mappers.ProductMapper;
import com.squares.products.models.ProductModel;
import com.squares.products.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDTO, productModel);
        ProductModel saved = productRepository.save(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(saved));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDTO> productsPage = this.productRepository.findAll(pageable)
                .map(product -> product.add(linkTo(methodOn(ProductController.class)
                        .getOneProduct(product.getId()))
                        .withSelfRel()))
                .map(productMapper::toResponse);

        return ResponseEntity.status(HttpStatus.OK).body(productsPage);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") String productId) {
        Optional<ProductModel> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }

        ProductModel productModel = product.get();
        productModel.add(linkTo(methodOn(ProductController.class).getAllProducts(Pageable.unpaged()))
                .withRel("products"));

        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponse(productModel));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(name = "id") String id, @RequestBody @Valid ProductRecordDTO productRecordDTO) {
        Optional<ProductModel> product = this.productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }

        var productModel = product.get();

        BeanUtils.copyProperties(productRecordDTO, productModel);
        ProductModel saved = productRepository.save(productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponse(saved));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(name = "id") String productId) {
        Optional<ProductModel> product = this.productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }

        this.productRepository.deleteById(productId);

        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }
}
