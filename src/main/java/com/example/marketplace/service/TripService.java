package com.example.marketplace.service;

import com.example.marketplace.dto.TripDTO;
import com.example.marketplace.dto.TripSearchDTO;
import com.example.marketplace.model.Booking;
import com.example.marketplace.model.Trip;
import com.example.marketplace.repository.BookingRepository;
import com.example.marketplace.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


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
                .orElseThrow(() -> new EntityNotFoundException("Trips not found with route_id=%s"
                        .formatted(routeId)));
    }

    // Получение поездок по типу транспорта
    public List<Trip> getTripsByTransportTypeId(Long transportTypeId) {
        return tripRepository.findByTransportTypeId(transportTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Trips not found with transport_type_id=%s"
                        .formatted(transportTypeId)));
    }

    // Получение поездок максимально близких к желаемой дате
    public List<Trip> getNearestTrips(TripSearchDTO tripSearchDTO) {
        List<Trip> trips = getTripsFromDTO(tripSearchDTO);
        trips.sort(Comparator.comparing(trip ->
                Math.abs(Duration.between(tripSearchDTO.getDepartureTime(), trip.getDepartureTime()).toMinutes())));
        return trips.stream()
                .filter(trip -> trip.getDepartureTime().isAfter(LocalDateTime.now()))
                .filter(this::hasAvailableSeats)
                .limit(5)
                .toList(); // Получение 5 ближайших поездок
    }

    private List<Trip> getTripsFromDTO(TripSearchDTO tripSearchDTO) {
        List<Trip> trips;
        if (tripSearchDTO.getRouteId() != null && tripSearchDTO.getTransportTypeId() != null) {
            trips = tripRepository
                    .findByTransportTypeIdAndRouteId(tripSearchDTO.getTransportTypeId(), tripSearchDTO.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Trips not found with transport_type_id=%s and route_id=%s"
                            .formatted(tripSearchDTO.getTransportTypeId(), tripSearchDTO.getRouteId())));
        } else if (tripSearchDTO.getRouteId() != null) {
            trips = getTripsByRouteId(tripSearchDTO.getRouteId());
        } else if (tripSearchDTO.getTransportTypeId() != null) {
            trips = getTripsByTransportTypeId(tripSearchDTO.getTransportTypeId());
        } else {
            trips = getAllTrips();
        }
        return trips;
    }

    // Получение поездок, сгруппированных по дате отправления
    public Map<LocalDate, List<Trip>> getTripsGroupedByDate(TripSearchDTO tripSearchDTO) {
        List<Trip> trips = getTripsFromDTO(tripSearchDTO);
        return trips.stream()
            .filter(trip -> trip.getDepartureTime().isAfter(LocalDateTime.now()))
            .filter(this::hasAvailableSeats)
            .sorted(Comparator.comparing(Trip::getDepartureTime))
            .collect(Collectors.groupingBy(
                    trip -> trip.getDepartureTime().toLocalDate(),
                    TreeMap::new,
                    Collectors.toList()));
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
        var trip = getTripById(id);
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
    public boolean hasAvailableSeats(Trip trip) {
        int bookedSeats = bookingRepository.countByTripAndStatus(trip, Booking.BookingStatus.CONFIRMED);
        return bookedSeats < trip.getSeatsAvailable();
    }
}

