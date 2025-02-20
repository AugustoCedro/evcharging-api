package org.example.evchargingapi.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.evchargingapi.model.ChargeStation;

@Schema(name = "Charging Session Request")
public record ChargingSessionRequestDTO(
        @NotNull
        Long stationId,
        @NotBlank
        String userEmail
) {
}
