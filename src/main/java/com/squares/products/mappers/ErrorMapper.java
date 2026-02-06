package com.squares.products.mappers;

import com.squares.products.dtos.ErrorResponseDTO;
import com.squares.products.exceptions.ApiError;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ErrorMapper {
    ErrorResponseDTO toResponse(ApiError error);
}
