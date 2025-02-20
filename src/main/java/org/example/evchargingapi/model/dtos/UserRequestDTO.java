package org.example.evchargingapi.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "User Request")
public record UserRequestDTO(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
