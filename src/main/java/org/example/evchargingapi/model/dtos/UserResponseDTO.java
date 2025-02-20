package org.example.evchargingapi.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User Response")
public record UserResponseDTO(
        Long id,
        String email
) {
}
