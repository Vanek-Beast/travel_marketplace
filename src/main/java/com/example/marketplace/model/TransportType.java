package com.example.marketplace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "transport_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название должно быть указано")
    @Size(min = 2, max = 75, message = "Название должно быть от 2 до 75 символов")
    @Column(nullable = false, unique = true)
    private String name;
}
