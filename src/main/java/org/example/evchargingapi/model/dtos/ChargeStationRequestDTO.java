package org.example.evchargingapi.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Charge Station Request")
public record ChargeStationRequestDTO(
        @NotBlank
        String location,
        @NotNull
        Double powerKw
) {
}
