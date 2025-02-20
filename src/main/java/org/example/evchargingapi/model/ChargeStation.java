package org.example.evchargingapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.evchargingapi.model.dtos.ChargeStationRequestDTO;
import org.example.evchargingapi.model.dtos.ChargeStationResponseDTO;

@Entity
@Table(name = "charge_station")
@Data
@NoArgsConstructor
public class ChargeStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    private Boolean available;

    @Column(name = "power_kw")
    private double powerKw;

    public ChargeStation(String location, double powerKw) {
        this.location = location;
        this.powerKw = powerKw;
    }

    public static ChargeStation toEntity(ChargeStationRequestDTO dto){
        var station = new ChargeStation();
        station.setLocation(dto.location());
        station.setPowerKw(dto.powerKw());
        return station;
    }

    public static ChargeStationResponseDTO toDTO(ChargeStation station){
        return new ChargeStationResponseDTO(
                station.getId(),
                station.getLocation(),
                station.getPowerKw(),
                station.getAvailable()
        );
    }


}
