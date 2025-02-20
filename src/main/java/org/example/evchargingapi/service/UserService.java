package org.example.evchargingapi.service;

import org.example.evchargingapi.exceptions.EntityAlreadyExistsException;
import org.example.evchargingapi.model.User;
import org.example.evchargingapi.model.enums.Role;
import org.example.evchargingapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;
    @Autowired
    PasswordEncoder encoder;

    public User save(User user){
        var optionalUser = repository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            throw new EntityAlreadyExistsException("Erro ao Cadastrar: Email já cadastrado!");
        }
        var encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        return repository.save(user);
    }

    public User saveAdmin(User user){
        var optionalUser = repository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            throw new EntityAlreadyExistsException("Erro ao Cadastrar: Email já cadastrado!");
        }
        var encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.ADMIN);
        return repository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public List<User> findByExample(Long id, String email) {
        var user = new User();
        user.setId(id);
        user.setEmail(email);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<User> userExample = Example.of(user,matcher);
        return repository.findAll(userExample);
    }
}
