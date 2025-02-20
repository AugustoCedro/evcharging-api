package org.example.evchargingapi.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(name = "Error")
public record ErrorDTO(
        Integer status,
        String message,
        List<ErrorFieldDTO> errors
) {
}
