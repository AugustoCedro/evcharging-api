package org.example.evchargingapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.evchargingapi.model.User;
import org.example.evchargingapi.model.dtos.UserRequestDTO;
import org.example.evchargingapi.model.dtos.UserResponseDTO;
import org.example.evchargingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.example.evchargingapi.config.CommonApiResponses;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "users")
@Tag(name = "User")

public class UserController implements GenericController {

    @Autowired
    UserService service;

    @PostMapping
    @Operation(summary = "Save",description = "save a new user")
    @ApiResponse(responseCode = "201",description = "Created with success")
    @CommonApiResponses.EntityAlreadyRegisteredErrorResponse
    @CommonApiResponses.ValidationErrorResponse
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserRequestDTO userDTO){
        var user = User.toEntity(userDTO);
        service.save(user);
        URI location = getURILocation(user.getId());
        return ResponseEntity.created(location).body(User.toDTO(user));
    }

    @DeleteMapping("{email}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Delete (ADMIN)",description = "delete User by Email")
    @ApiResponse(responseCode = "204",description = "Deleted with success")
    @CommonApiResponses.EntityNotFoundErrorResponse
    @CommonApiResponses.UnauthorizedResponse
    @CommonApiResponses.ForbiddenErrorResponse
    public ResponseEntity<Void> deleteUser(@PathVariable String email){
        var optionalUser = service.findByEmail(email);
        if(optionalUser.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        service.delete(optionalUser.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Get (ADMIN)",description = "Get all Users or query by parameters")
    @ApiResponse(responseCode = "200",description = "Success")
    @CommonApiResponses.UnauthorizedResponse
    @CommonApiResponses.ForbiddenErrorResponse
    public ResponseEntity<List<UserResponseDTO>> getUser(
            @RequestParam(name = "id",required = false) Long id,
            @RequestParam(name = "email",required = false) String email
    ){
        List<User> users = service.findByExample(id,email);
        List<UserResponseDTO> usersDTO = users.stream().map(User::toDTO).toList();
        return ResponseEntity.ok(usersDTO);
    }
}
