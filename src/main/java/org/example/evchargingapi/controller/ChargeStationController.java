package org.example.evchargingapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.evchargingapi.model.ChargeStation;
import org.example.evchargingapi.model.dtos.ChargeStationRequestDTO;
import org.example.evchargingapi.model.dtos.ChargeStationResponseDTO;
import org.example.evchargingapi.service.ChargeStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.example.evchargingapi.config.CommonApiResponses;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "stations")
@Tag(name = "Charge Station")
public class ChargeStationController implements GenericController{

    @Autowired
    ChargeStationService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Save (ADMIN)",description = "save a new charge station")
    @ApiResponse(responseCode = "201",description = "Created with success")
    @CommonApiResponses.EntityAlreadyRegisteredErrorResponse
    @CommonApiResponses.ValidationErrorResponse
    @CommonApiResponses.UnauthorizedResponse
    @CommonApiResponses.ForbiddenErrorResponse
    public ResponseEntity<ChargeStationResponseDTO> saveStation(@RequestBody @Valid ChargeStationRequestDTO stationDTO){
        var station = ChargeStation.toEntity(stationDTO);
        service.save(station);
        URI location = getURILocation(station.getId());
        return ResponseEntity.created(location).body(ChargeStation.toDTO(station));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Toggle availability (ADMIN)",description = "Toggle charging station availability")
    @ApiResponse(responseCode = "200",description = "Updated with success")
    @CommonApiResponses.EntityNotFoundErrorResponse
    @CommonApiResponses.UnauthorizedResponse
    @CommonApiResponses.ForbiddenErrorResponse
    public ResponseEntity<ChargeStationResponseDTO> changeStationAvailable(@PathVariable Long id){
        var station = service.findById(id);
        station.setAvailable(!station.getAvailable());
        service.update(station);
        return ResponseEntity.ok(ChargeStation.toDTO(station));
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Get by Availability",description = "Get all Charge Stations by Availability True")
    @ApiResponse(responseCode = "200",description = "Success")
    @CommonApiResponses.UnauthorizedResponse
    public ResponseEntity<List<ChargeStationResponseDTO>> getAvailableStations(){
        List<ChargeStation> stations = service.findAllByAvailable();
        List<ChargeStationResponseDTO> stationsDTO = stations.stream().map(ChargeStation::toDTO).toList();
        return ResponseEntity.ok(stationsDTO);
    }
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Get All (ADMIN)",description = "Get all Charge Stations")
    @ApiResponse(responseCode = "200",description = "Success")
    @CommonApiResponses.UnauthorizedResponse
    @CommonApiResponses.ForbiddenErrorResponse
    public ResponseEntity<List<ChargeStationResponseDTO>> getAllStations(){
        List<ChargeStation> stations = service.findAll();
        List<ChargeStationResponseDTO> stationsDTO = stations.stream().map(ChargeStation::toDTO).toList();
        return ResponseEntity.ok(stationsDTO);
    }

}
