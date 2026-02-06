package com.squares.products.dtos;

import java.time.Instant;

public record ProductResponseDTO(
        String id,
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
        Instant createdAt,
        Instant updatedAt,
        String imageUrl,
        String barcode
) {
}
