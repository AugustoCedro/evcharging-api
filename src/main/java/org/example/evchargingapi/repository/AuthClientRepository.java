package org.example.evchargingapi.repository;

import org.example.evchargingapi.model.AuthClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthClientRepository extends JpaRepository<AuthClient,Long> {
    AuthClient findByClientId(String clientId);
}
