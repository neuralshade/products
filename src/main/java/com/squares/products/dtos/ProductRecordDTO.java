package com.squares.products.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductRecordDTO(
        @NotBlank String sku,
        @NotBlank String name,
        @Size(max = 1000) String description,
        @Size(max = 255) String category,
        @Size(max = 255) String brand,
        @Size(max = 50) String unit,
        @NotNull @PositiveOrZero Integer stockQuantity,
        @NotNull @PositiveOrZero Integer minStock,
        @PositiveOrZero Float costPrice,
        @PositiveOrZero Float salePrice,
        @NotNull @Positive Float value,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull Boolean active,
        @Size(max = 1024) String imageUrl,
        @Size(max = 128) String barcode
) {
}
