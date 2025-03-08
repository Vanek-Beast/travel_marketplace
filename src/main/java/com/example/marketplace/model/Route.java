package com.example.marketplace.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "routes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Пункт отправления не может быть пустым")
    @Size(min = 1, max = 75, message = "Пункт отправления должен содержать от {min} до {max} символов")
    @Column(nullable = false)
    private String origin;

    @NotBlank(message = "Пункт назначения не может быть пустым")
    @Size(min = 1, max = 75, message = "Пункт назначения должен содержать от {min} до {max} символов")
    @Column(nullable = false)
    private String destination;
}
