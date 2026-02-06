package com.squares.products.mappers;

import com.squares.products.dtos.ProductResponseDTO;
import com.squares.products.models.ProductModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO toResponse(ProductModel model);
}
