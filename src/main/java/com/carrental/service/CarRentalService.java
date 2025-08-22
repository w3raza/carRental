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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarRentalService {
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public List<Car> getAvailableCars(CarType type) {
        LocalDateTime now = LocalDateTime.now();
        List<Car> carsOfType = carRepository.findByType(type);
        List<Reservation> activeReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getEndDateTime().isAfter(now))
                .toList();

        List<Long> reservedCarIds = activeReservations.stream()
                .map(r -> r.getCar().getId())
                .toList();

        return carsOfType.stream()
                .filter(car -> !reservedCarIds.contains(car.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Reservation reserveCar(CarType type, LocalDateTime startDateTime, int numberOfDays) {
        List<Car> availableCars = carRepository.findByTypeAndAvailableTrue(type);
        if (availableCars.isEmpty()) {
            throw new RuntimeException("No available cars of type " + type);
        }
        Car car = availableCars.get(0);
        car.setAvailable(false);
        carRepository.save(car);
        Reservation reservation = Reservation.builder()
                .car(car)
                .startDateTime(startDateTime)
                .endDateTime(startDateTime.plusDays(numberOfDays))
                .numberOfDays(numberOfDays)
                .build();
        return reservationRepository.save(reservation);
    }
}
