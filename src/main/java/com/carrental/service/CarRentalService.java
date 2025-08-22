package com.carrental.service;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarRentalService {
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public CarRentalService(CarRepository carRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Car> getAvailableCars(CarType type) {
        return carRepository.findByTypeAndAvailableTrue(type);
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
                .numberOfDays(numberOfDays)
                .build();
        return reservationRepository.save(reservation);
    }
}
