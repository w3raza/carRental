package com.carrental.controller;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.service.CarRentalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@AllArgsConstructor
public class CarRentalController {
    private final CarRentalService carRentalService;

    @GetMapping("/available")
    public List<Car> getAvailableCars(@RequestParam CarType type) {
        return carRentalService.getAvailableCars(type);
    }

    @PostMapping("/reserve")
    public Reservation reserveCar(@RequestParam CarType type,
                                  @RequestParam LocalDateTime startDateTime,
                                  @RequestParam int numberOfDays) {
        return carRentalService.reserveCar(type, startDateTime, numberOfDays);
    }
}