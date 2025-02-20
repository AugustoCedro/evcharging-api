package org.example.evchargingapi.security;

import lombok.RequiredArgsConstructor;
import org.example.evchargingapi.model.User;
import org.example.evchargingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService service;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        var optionalUser = service.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User/password incorrect");
        }
        var user = optionalUser.get();
        String encodedPassword = user.getPassword();
        boolean isPasswordsMatching = encoder.matches(password,encodedPassword);
        if(isPasswordsMatching){
            return new CustomAuthentication(user);
        }
        throw new UsernameNotFoundException("User/password incorrect");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
