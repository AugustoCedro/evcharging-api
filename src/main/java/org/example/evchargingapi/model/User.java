package org.example.evchargingapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.evchargingapi.model.dtos.UserRequestDTO;
import org.example.evchargingapi.model.dtos.UserResponseDTO;
import org.example.evchargingapi.model.enums.Role;

@Entity
@Table(name = "Customer")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public static User toEntity(UserRequestDTO dto){
        var user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    public static UserResponseDTO toDTO(User user){
        return new UserResponseDTO(user.getId(), user.getEmail());
    }

}
