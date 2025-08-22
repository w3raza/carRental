package com.carrental.controller;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.service.CarRentalService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarRentalController {
    private final CarRentalService carRentalService;

    public CarRentalController(CarRentalService carRentalService) {
        this.carRentalService = carRentalService;
    }

    @GetMapping("/available")
    public List<Car> getAvailableCars(@RequestParam CarType type) {
        return carRentalService.getAvailableCars(type);
    }

    @PostMapping("/reserve")
    public Reservation reserveCar(@RequestParam CarType type,
                                  @RequestParam String startDateTime,
                                  @RequestParam int numberOfDays) {
        LocalDateTime start = LocalDateTime.parse(startDateTime);
        return carRentalService.reserveCar(type, start, numberOfDays);
    }
}