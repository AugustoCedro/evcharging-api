package org.example.evchargingapi.service;

import org.example.evchargingapi.exceptions.EntityAlreadyExistsException;
import org.example.evchargingapi.model.AuthClient;
import org.example.evchargingapi.model.enums.Role;
import org.example.evchargingapi.repository.AuthClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthClientService {
    @Autowired
    AuthClientRepository repository;
    @Autowired
    PasswordEncoder encoder;

    public AuthClient findByClientId(String clientId){
        return repository.findByClientId(clientId);
    }

    public AuthClient save(AuthClient authClient){
        var client = repository.findByClientId(authClient.getClientId());
        if(client != null){
            throw new EntityAlreadyExistsException("Erro ao Cadastrar: Email já cadastrado!");
        }
        var encodedPassword = encoder.encode(authClient.getClientSecret());
        authClient.setClientSecret(encodedPassword);
        authClient.setScope(Role.ADMIN.name());
        authClient.setRedirectURI("http://localhost:8080/authorized");
        return repository.save(authClient);
    }
}
