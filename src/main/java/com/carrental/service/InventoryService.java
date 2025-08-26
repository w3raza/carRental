package com.carrental.service;

import com.carrental.model.CarType;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class InventoryService {
    private final Map<CarType, Integer> capacity = new EnumMap<>(CarType.class);

    public InventoryService() {
        capacity.put(CarType.SEDAN, 5);
        capacity.put(CarType.SUV, 3);
        capacity.put(CarType.VAN, 2);
    }

    public int capacityOf(CarType type) { return capacity.getOrDefault(type, 0); }
}
