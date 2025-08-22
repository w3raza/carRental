package com.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CarType type;

    private boolean available;

    public Car() {}

    public Car(Long id, CarType type, boolean available) {
        this.id = id;
        this.type = type;
        this.available = available;
    }
}
