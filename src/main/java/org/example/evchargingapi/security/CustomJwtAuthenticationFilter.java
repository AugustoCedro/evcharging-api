package org.example.evchargingapi.security;

import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.evchargingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    UserService service;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(mustConvert(authentication)){
            String login = authentication.getName();
            var optionalUser = service.findByEmail(login);
            if(optionalUser.isPresent()){
                authentication = new CustomAuthentication(optionalUser.get());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }
    private boolean mustConvert(Authentication authentication){
        return authentication != null && authentication instanceof JwtAuthenticationToken;
    }
}
