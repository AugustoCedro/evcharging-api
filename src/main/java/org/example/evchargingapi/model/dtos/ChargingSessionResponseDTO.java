package org.example.evchargingapi.model.dtos;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(name = "Charging Session Response")
public record ChargingSessionResponseDTO(
        Long id,
        ChargeStationResponseDTO station,
        UserResponseDTO user,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Double energyCost,
        Double sessionPrice
) {
}
