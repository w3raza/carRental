package com.carrental;

import com.carrental.service.InventoryService;
import org.junit.jupiter.api.Test;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.service.CarRentalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class CarRentalApplicationTests {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CarRentalService carRentalService;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        reservationRepository.flush();
        carRepository.deleteAll();
        carRepository.flush();

        carRentalService.save(CarType.SEDAN);
        carRentalService.save(CarType.SEDAN);
        carRentalService.save(CarType.SUV);
        carRentalService.save(CarType.VAN);
    }


    @Test
    void shouldReserveCarOfGivenTypeForGivenDays() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();

        // When
        Reservation reservation = carRentalService.reserveCar(CarType.SEDAN, startDate, 3);

        // Then
        Assertions.assertNotNull(reservation);
        Assertions.assertEquals(CarType.SEDAN, reservation.getCar().getType());
        Assertions.assertEquals(3, reservation.getNumberOfDays());
    }

    @Test
    void shouldNotReserveCarIfNoneAvailable() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();
        carRentalService.reserveCar(CarType.SEDAN, startDate, 2);
        carRentalService.reserveCar(CarType.SEDAN, startDate.plusDays(1), 2);

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> carRentalService.reserveCar(CarType.SEDAN, startDate.plusDays(1), 2));
    }

    @Test
    void shouldReserveDifferentTypesOfCars() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();

        // When
        Reservation suvReservation = carRentalService.reserveCar(CarType.SUV, startDate, 1);
        Reservation vanReservation = carRentalService.reserveCar(CarType.VAN, startDate, 1);

        // Then
        Assertions.assertEquals(CarType.SUV, suvReservation.getCar().getType());
        Assertions.assertEquals(CarType.VAN, vanReservation.getCar().getType());
    }

    @Test
    void shouldReturnAvailableCarsAfterReservation() {
        // Given
        List<Car> sedansBefore = carRentalService.getAvailableCars(CarType.SEDAN);
        Assertions.assertEquals(2, sedansBefore.size());

        // When
        carRentalService.reserveCar(CarType.SEDAN, LocalDateTime.now(), 1);
        List<Car> sedansAfter = carRentalService.getAvailableCars(CarType.SEDAN);

        // Then
        Assertions.assertEquals(1, sedansAfter.size());
    }

    @Test
    void shouldNotAddMoreCarsThanCapacity() {
        CarType type = CarType.SEDAN;

        int capacity = inventoryService.capacityOf(type);

        long existing = carRepository.countByType(type);

        for (int i = 0; i < capacity - existing; i++) {
            Car car = carRentalService.save(type);
            Assertions.assertNotNull(car.getId());
        }

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> carRentalService.save(type));

        Assertions.assertTrue(exception.getMessage().contains("Limit reached"));
    }

}