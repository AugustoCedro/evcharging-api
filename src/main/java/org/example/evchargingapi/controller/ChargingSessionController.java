package org.example.evchargingapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.evchargingapi.model.ChargingSession;
import org.example.evchargingapi.model.dtos.ChargingSessionRequestDTO;
import org.example.evchargingapi.model.dtos.ChargingSessionResponseDTO;
import org.example.evchargingapi.model.dtos.ErrorDTO;
import org.example.evchargingapi.service.ChargingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.example.evchargingapi.config.CommonApiResponses;

import java.net.URI;

import java.util.List;

@RestController
@RequestMapping(value = "sessions")
@Tag(name = "Charging Session")
public class ChargingSessionController implements GenericController{

    @Autowired
    ChargingSessionService service;

    @PostMapping("/start")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Start",description = "Start a new Charging Session")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "Session Started with success"),
            @ApiResponse(responseCode = "400",description = "Station isn't available ERROR")
    })
    @CommonApiResponses.EntityNotFoundErrorResponse
    @CommonApiResponses.ValidationErrorResponse
    @CommonApiResponses.UnauthorizedResponse
    public ResponseEntity<ChargingSessionResponseDTO> startSession(@RequestBody @Valid ChargingSessionRequestDTO sessionDTO){
        var session = service.start(sessionDTO);
        URI location = getURILocation(session.getId());
        return ResponseEntity.created(location).body(ChargingSession.toDTO(session));
    }

    @PostMapping("/end/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "End",description = "End a Charging Session")
    @ApiResponse(responseCode = "201",description = "Session Finished with success")
    @CommonApiResponses.EntityNotFoundErrorResponse
    @CommonApiResponses.UnauthorizedResponse
    public ResponseEntity<ChargingSessionResponseDTO> endSession(@PathVariable Long id){
        var session = service.end(id);
        URI location = getURILocation(session.getId());
        return ResponseEntity.created(location).body(ChargingSession.toDTO(session));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Get (ADMIN)",description = "Get all Charging Session or query by parameters")
    @ApiResponse(responseCode = "200",description = "Success")
    @CommonApiResponses.EntityNotFoundErrorResponse
    @CommonApiResponses.UnauthorizedResponse
    @CommonApiResponses.ForbiddenErrorResponse
    public ResponseEntity<List<ChargingSessionResponseDTO>> getSession(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "stationId", required = false) Long stationId,
            @RequestParam(name = "userEmail", required = false) String userEmail
    ){
        List<ChargingSession> sessions = service.findByExample(id,stationId,userEmail);
        List<ChargingSessionResponseDTO> sessionsDTO = sessions.stream().map(ChargingSession::toDTO).toList();
        return ResponseEntity.ok(sessionsDTO);
    }

}
