package org.example.evchargingapi.security;


import org.example.evchargingapi.service.AuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    @Autowired
    AuthClientService service;
    @Autowired
    TokenSettings tokenSettings;
    @Autowired
    ClientSettings clientSettings;

    @Override
    public void save(RegisteredClient registeredClient) {
    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var authClient = service.findByClientId(clientId);
        if(authClient == null){
            return null;
        }
        return RegisteredClient
                .withId(authClient.getId().toString())
                .clientId(authClient.getClientId())
                .clientSecret(authClient.getClientSecret())
                .redirectUri(authClient.getRedirectURI())
                .scope(authClient.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();
    }
}
