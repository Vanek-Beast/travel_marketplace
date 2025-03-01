package com.example.marketplace.repository;

import com.example.marketplace.model.TransportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportTypeRepository extends JpaRepository<TransportType, Long> {
    Optional<TransportType> findByName(String name);
}
