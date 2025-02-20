package org.example.evchargingapi.repository;

import org.example.evchargingapi.model.ChargeStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChargeStationRepository extends JpaRepository<ChargeStation,Long> {

    Optional<ChargeStation> findByLocation(String location);

    List<ChargeStation> findAllByAvailable(Boolean available);
}
