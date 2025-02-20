package org.example.evchargingapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public interface GenericController {
    default URI getURILocation(Long id){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
