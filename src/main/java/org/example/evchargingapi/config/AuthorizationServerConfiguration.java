package org.example.evchargingapi.config;


import org.example.evchargingapi.security.CustomAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer auth2AuthorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
        http
            .securityMatcher(auth2AuthorizationServerConfigurer.getEndpointsMatcher())
            .with(auth2AuthorizationServerConfigurer,authorizationServer ->
                    authorizationServer.oidc(Customizer.withDefaults())
            )
            .oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()))
            .authorizeHttpRequests(authorize ->
                    authorize.anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                .build();
    }
    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build();
    }
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(){
        return context -> {
            var principal = context.getPrincipal();
            if(principal instanceof CustomAuthentication authentication){
                OAuth2TokenType tokenType = context.getTokenType();
                if(OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)){
                    Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
                    List<String> authoritiesList = authorities.stream().map(GrantedAuthority::getAuthority).toList();
                    context.getClaims()
                            .claim("authorities",authoritiesList);
                }
            }
        };
    }




}
