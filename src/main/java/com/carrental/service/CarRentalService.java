package com.carrental.service;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarRentalService {
    private final CarRepository carRepository;

    public CarRentalService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAvailableCars(CarType type) {
        return carRepository.findByTypeAndAvailableTrue(type);
    }
}
