package com.example.marketplace.model;

import com.example.marketplace.validator.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Фамилия обзательна для заполнения")
    @Size(min = 1, max = 75, message = "Surname must be between {min} and {max} characters")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "Имя обзательно для заполнения")
    @Size(min = 1, max = 75, message = "Name must be between {min} and {max} characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Электронная почта обзательна для заполнения")
    @Email(message = "Электронная почта должна быть в формате example@example.com")
    @Column(nullable = false, unique = true)
    private String email;

    @ValidPassword
    @Column(nullable = false)
    private String password;
}