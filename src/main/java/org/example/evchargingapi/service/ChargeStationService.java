package org.example.evchargingapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.evchargingapi.exceptions.EntityAlreadyExistsException;
import org.example.evchargingapi.model.ChargeStation;
import org.example.evchargingapi.repository.ChargeStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeStationService {
    @Autowired
    ChargeStationRepository repository;

    public ChargeStation save(ChargeStation station){
        var optionalStation = repository.findByLocation(station.getLocation());
        if(optionalStation.isPresent()){
            throw new EntityAlreadyExistsException("Erro ao Cadastrar: Estação de Carga já cadastrada!");
        }
        station.setAvailable(Boolean.TRUE);
        return repository.save(station);
    }

    public ChargeStation findById(Long id) {
        var optionalStation =  repository.findById(id);
        if(optionalStation.isEmpty()){
            throw new EntityNotFoundException("Erro ao Buscar: Estação de Carga não encontrada!");
        }
        return optionalStation.get();
    }

    public ChargeStation update(ChargeStation station){
        return repository.save(station);
    }

    public List<ChargeStation> findAllByAvailable() {
        return repository.findAllByAvailable(Boolean.TRUE);
    }

    public List<ChargeStation> findAll() {
        return repository.findAll();
    }

    public Boolean stationIsAvailable(ChargeStation station){
       return station.getAvailable();
    }

}
