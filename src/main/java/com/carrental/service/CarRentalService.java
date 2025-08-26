package com.carrental.service;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarRentalService {
    private final InventoryService inventory;
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Car save(CarType type) {
        int capacity = inventory.capacityOf(type);
        long existing = carRepository.countByType(type);

        if (existing >= capacity) {
            throw new RuntimeException("Cannot add more cars of type " + type + ". Limit reached: " + capacity);
        }

        Car car = new Car(null, type);
        return carRepository.save(car);
    }

    public List<Car> getAvailableCars(CarType type) {
        LocalDateTime now = LocalDateTime.now();

        List<Car> carsOfType = carRepository.findByType(type);

        List<Long> reservedCarIds = reservationRepository.findReservedCarIds(type, now, now);
        Set<Long> reservedIdsSet = new HashSet<>(reservedCarIds);

        return carsOfType.stream()
                .filter(car -> !reservedIdsSet.contains(car.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Reservation reserveCar(CarType type, LocalDateTime startDateTime, int numberOfDays) {
        LocalDateTime requestedEnd = startDateTime.plusDays(numberOfDays);

        List<Car> carsOfType = carRepository.findByType(type);
        if (carsOfType.isEmpty()) {
            throw new RuntimeException("No cars of type " + type + " exist in inventory");
        }

        List<Long> reservedCarIds = reservationRepository.findReservedCarIds(type, startDateTime, requestedEnd);
        Set<Long> reservedIdsSet = new HashSet<>(reservedCarIds);

        Optional<Car> availableCar = carsOfType.stream()
                .filter(car -> !reservedIdsSet.contains(car.getId()))
                .findFirst();

        if (availableCar.isEmpty()) {
            throw new RuntimeException("No available cars of type " + type + " for the requested period");
        }

        Reservation reservation = Reservation.builder()
                .car(availableCar.get())
                .startDateTime(startDateTime)
                .endDateTime(requestedEnd)
                .numberOfDays(numberOfDays)
                .build();

        return reservationRepository.save(reservation);
    }
}
