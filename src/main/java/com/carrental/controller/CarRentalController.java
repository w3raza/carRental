package com.carrental.controller;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.service.CarRentalService;
import org.springframework.web.bind.annotation.*;

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
}