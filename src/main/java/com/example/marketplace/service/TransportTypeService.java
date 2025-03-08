package com.example.marketplace.service;

import com.example.marketplace.model.TransportType;
import com.example.marketplace.repository.TransportTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportTypeService {

    private final TransportTypeRepository transportTypeRepository;

    @Autowired
    public TransportTypeService(TransportTypeRepository transportTypeRepository) {
        this.transportTypeRepository = transportTypeRepository;
    }

    // Создание типа транспорта
    public TransportType createTransportType(TransportType transportType) {
        if (transportTypeRepository.findByName(transportType.getName()).isPresent()) {
            throw new IllegalArgumentException("Transport %s already exists"
                    .formatted(transportType.getName()));
        }
        return transportTypeRepository.save(transportType);
    }

    // Получение всех типов транспорта
    public List<TransportType> getAllTransportTypes() {
        return transportTypeRepository.findAll();
    }

    // Поулчение типа транспорта по id
    public TransportType getTransportTypeById(Long id) {
        return transportTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transport type not found by id=%s"
                        .formatted(id)));
    }

    // Обновление типа транспорта
    @Transactional
    public TransportType updateTransportType(Long id, TransportType updatedTransportType) {
        var transportType = getTransportTypeById(id);
        transportType.setName(updatedTransportType.getName());
        return transportType;
    }

    // Удаление типа транспорта
    public void deleteTransportType(Long id) {
        transportTypeRepository.deleteById(id);
    }
}