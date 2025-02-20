package org.example.evchargingapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.evchargingapi.model.dtos.ChargeStationResponseDTO;
import org.example.evchargingapi.model.dtos.ChargingSessionRequestDTO;
import org.example.evchargingapi.model.dtos.ChargingSessionResponseDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "charging_session")
@Data
public class ChargingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private ChargeStation station;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;

    @JoinColumn(name = "energy_cost")
    private Double energyCost = 0.80;

    @JoinColumn(name = "start_time")
    private LocalDateTime startTime;

    @JoinColumn(name = "end_time")
    private LocalDateTime endTime;

    private Double sessionPrice;

    public static ChargingSessionResponseDTO toDTO(ChargingSession session){
        return new ChargingSessionResponseDTO(
                session.getId(),
                ChargeStation.toDTO(session.getStation()),
                User.toDTO(session.getUser()),
                session.getStartTime(),
                session.getEndTime(),
                session.getEnergyCost(),
                session.getSessionPrice()
        );
    }

}
