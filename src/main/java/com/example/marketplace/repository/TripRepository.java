package com.example.marketplace.repository;

import com.example.marketplace.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<List<Trip>> findByRouteId(Long routeId);
    Optional<List<Trip>> findByTransportTypeId(Long transportTypeId);
}