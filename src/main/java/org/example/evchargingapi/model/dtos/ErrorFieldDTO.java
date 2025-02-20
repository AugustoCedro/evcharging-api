package org.example.evchargingapi.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Error Fields")
public record ErrorFieldDTO(
        String field,
        String error
) {
}
