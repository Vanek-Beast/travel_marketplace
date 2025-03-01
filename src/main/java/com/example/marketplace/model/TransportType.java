package com.example.marketplace.model;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String name;
}
