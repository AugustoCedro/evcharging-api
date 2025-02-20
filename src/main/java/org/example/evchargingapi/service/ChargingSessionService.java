package org.example.evchargingapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.evchargingapi.exceptions.EntityIsNotAvailableException;
import org.example.evchargingapi.model.ChargingSession;
import org.example.evchargingapi.model.User;
import org.example.evchargingapi.model.dtos.ChargingSessionRequestDTO;
import org.example.evchargingapi.repository.ChargingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChargingSessionService {
    @Autowired
    ChargingSessionRepository repository;
    @Autowired
    ChargeStationService stationService;
    @Autowired
    UserService userService;


    public ChargingSession start(ChargingSessionRequestDTO sessionDTO){
        var session = new ChargingSession();
        var optionalUser = userService.findByEmail(sessionDTO.userEmail());
        var station = stationService.findById(sessionDTO.stationId());
        if(optionalUser.isEmpty()){
            throw new EntityNotFoundException("Erro ao Buscar: Usuário não encontrado!");
        }
        if(!stationService.stationIsAvailable(station)){
            throw new EntityIsNotAvailableException("Erro ao criar sessão: Estação Indisponível");
        }
        var user = optionalUser.get();

        session.setStartTime(LocalDateTime.now());
        session.setStation(station);
        session.setUser(user);
        repository.save(session);
        return session;
    }

    private Double sessionPrice(ChargingSession session){
        long hours = Duration.between(session.getStartTime(), session.getEndTime()).toHours();
        System.out.println(hours);
        double energyConsumed = session.getStation().getPowerKw() * hours;
        System.out.println(energyConsumed);
        return energyConsumed * session.getEnergyCost();
    }

    public ChargingSession end(Long id){
        var optionalSession = repository.findById(id);
        if(optionalSession.isPresent()){
            var session = optionalSession.get();
            session.setEndTime(LocalDateTime.now());
            session.setSessionPrice(sessionPrice(session));
            return repository.save(session);
        }
        throw new EntityNotFoundException("Erro ao Buscar: Sessão não encontrada!");
    }

    public List<ChargingSession> findByExample(Long id,Long stationId,String userEmail){
        var session = new ChargingSession();

        if(stationId != null) {
            var station = stationService.findById(stationId);
            if (!stationService.stationIsAvailable(station)) {
                throw new EntityIsNotAvailableException("Erro ao criar sessão: Estação Indisponível");
            }
            session.setStation(station);
        }
        if(userEmail != null){
            var optionalUser = userService.findByEmail(userEmail);
            if(optionalUser.isEmpty()){
                throw new EntityNotFoundException("Erro ao Buscar: Usuário não encontrado!");
            }
            session.setUser(optionalUser.get());
        }
        session.setId(id);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<ChargingSession> sessionExample = Example.of(session,matcher);
        return repository.findAll(sessionExample);
    }

}
