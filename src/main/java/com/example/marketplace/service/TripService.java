package com.example.marketplace.service;

import com.example.marketplace.dto.TripDTO;
import com.example.marketplace.model.Booking;
import com.example.marketplace.model.Trip;
import com.example.marketplace.repository.BookingRepository;
import com.example.marketplace.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.util.List;


@Service
public class TripService {

    private final TripRepository tripRepository;
    private final RouteService routeService;
    private final TransportTypeService transportTypeService;
    private final BookingRepository bookingRepository;

    @Autowired
    public TripService(
            TripRepository tripRepository,
            RouteService routeService,
            TransportTypeService transportTypeService, BookingRepository bookingRepository) {
        this.tripRepository = tripRepository;
        this.routeService = routeService;
        this.transportTypeService = transportTypeService;
        this.bookingRepository = bookingRepository;
    }

    // Получение всех поездок
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    // Получение поездки по id
    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found by id=%s"
                        .formatted(id)));
    }

    // Получение поездок по маршруту
    public List<Trip> getTripsByRouteId(Long routeId) {
        return tripRepository.findByRouteId(routeId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with route_id=%s"
                        .formatted(routeId)));
    }

    // Получение поездок по типу транспорта
    public List<Trip> getTripsByTransportTypeId(Long transportTypeId) {
        return tripRepository.findByTransportTypeId(transportTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with transport_type_id=%s"
                        .formatted(transportTypeId)));
    }

    // Создание поездки
    public Trip createTrip(TripDTO tripDTO) {
        return tripRepository.save(Trip.builder()
                .route(routeService.getRouteById(tripDTO.getRouteId()))
                .transportType(transportTypeService.getTransportTypeById(tripDTO.getTransportTypeId()))
                .departureTime(tripDTO.getDepartureTime())
                .arrivalTime(tripDTO.getArrivalTime())
                .price(tripDTO.getPrice().setScale(2, RoundingMode.HALF_UP))
                .seatsAvailable(tripDTO.getSeatsAvailable())
                .build());
    }

    // Обновление поездки
    @Transactional
    public Trip updateTrip(Long id, TripDTO tripDTO) {
        var trip = tripRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found by id=%s"
                        .formatted(id)));
        trip.setRoute(routeService.getRouteById(tripDTO.getRouteId()));
        trip.setTransportType(transportTypeService.getTransportTypeById(tripDTO.getTransportTypeId()));
        trip.setDepartureTime(tripDTO.getDepartureTime());
        trip.setArrivalTime(tripDTO.getArrivalTime());
        trip.setPrice(tripDTO.getPrice().setScale(2, RoundingMode.HALF_UP));
        trip.setSeatsAvailable(tripDTO.getSeatsAvailable());
        return trip;
    }

    // Удаление поездки
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }

    // Проверка наличия доступных мест для поездки
    public boolean hasAvailableSeats(Long tripId) {
        Trip trip = getTripById(tripId);
        int bookedSeats = bookingRepository.countByTripsContainsAndStatus(trip, Booking.BookingStatus.CONFIRMED);
        return bookedSeats < trip.getSeatsAvailable();
    }
}

