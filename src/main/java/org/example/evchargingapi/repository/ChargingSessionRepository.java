package org.example.evchargingapi.repository;

import org.example.evchargingapi.model.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession,Long> {

}
