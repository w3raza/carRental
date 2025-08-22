package com.carrental.config;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initCars(CarRepository carRepository) {
        return args -> {
            carRepository.save(new Car(CarType.SEDAN, true));
            carRepository.save(new Car(CarType.SUV, true));
            carRepository.save(new Car(CarType.VAN, false));
        };
    }
}