package org.example.evchargingapi.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Charge Station Response")
public record ChargeStationResponseDTO (
        Long id,
        String location,
        Double powerKw,
        boolean available
){
}
