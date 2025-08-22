package com.carrental.repository;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByTypeAndAvailableTrue(CarType type);
    long countByType(CarType type);
}
