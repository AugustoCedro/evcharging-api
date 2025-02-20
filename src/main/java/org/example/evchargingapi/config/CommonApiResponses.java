package org.example.evchargingapi.config;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.example.evchargingapi.model.dtos.ErrorDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommonApiResponses {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponse(
            responseCode = "422",
            description = "Validation ERROR",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @interface ValidationErrorResponse {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized ERROR"
    )
    @interface UnauthorizedResponse {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponse(
            responseCode = "404",
            description = "Entity Not Found ERROR",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @interface EntityNotFoundErrorResponse{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden ERROR",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @interface ForbiddenErrorResponse{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ApiResponse(
            responseCode = "409",
            description = "Entity Already Registered ERROR",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @interface EntityAlreadyRegisteredErrorResponse{}

}
