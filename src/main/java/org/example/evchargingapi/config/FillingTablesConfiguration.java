package org.example.evchargingapi.config;

import lombok.RequiredArgsConstructor;
import org.example.evchargingapi.model.AuthClient;
import org.example.evchargingapi.model.ChargeStation;
import org.example.evchargingapi.model.ChargingSession;
import org.example.evchargingapi.model.User;
import org.example.evchargingapi.model.dtos.ChargingSessionRequestDTO;
import org.example.evchargingapi.service.AuthClientService;
import org.example.evchargingapi.service.ChargeStationService;
import org.example.evchargingapi.service.ChargingSessionService;
import org.example.evchargingapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FillingTablesConfiguration implements CommandLineRunner{

    private final UserService userService;
    private final ChargeStationService stationService;
    private final ChargingSessionService sessionService;
    private final AuthClientService authClientService;

    @Override
    public void run(String... args) throws Exception {

        userService.saveAdmin(new User("admin@admin.com","admin123"));
        userService.save(new User("user@user.com","user123"));
        authClientService.save(new AuthClient("client@client.com","client123"));

        stationService.save(new ChargeStation("Location 17",17.0));
        stationService.save(new ChargeStation("Location 21",7.0));
        stationService.save(new ChargeStation("Location 30",10.0));
    }
}
