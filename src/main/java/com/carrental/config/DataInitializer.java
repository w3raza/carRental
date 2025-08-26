package com.carrental.config;

import com.carrental.model.CarType;
import com.carrental.service.CarRentalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initCars(CarRentalService carRentalService) {
        return args -> {
            carRentalService.save(CarType.SEDAN);
            carRentalService.save(CarType.SUV);
            carRentalService.save(CarType.SUV);
            carRentalService.save(CarType.VAN);
        };
    }
}