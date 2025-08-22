package com.carrental;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.carrental.model.Car;
import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.service.CarRentalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import java.time.LocalDateTime;
import java.util.List;


@DataJpaTest
@ContextConfiguration(classes = {com.carrental.CarRentalApplication.class, com.carrental.service.CarRentalService.class})
class CarRentalApplicationTests {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    private CarRentalService carRentalService;

    @BeforeEach
    void setUp() {
        carRentalService = new CarRentalService(carRepository, reservationRepository);
        carRepository.deleteAll();
        reservationRepository.deleteAll();

        carRepository.save(new Car(null, CarType.SEDAN, true));
        carRepository.save(new Car(null, CarType.SEDAN, true));
        carRepository.save(new Car(null, CarType.SUV, true));
        carRepository.save(new Car(null, CarType.VAN, true));
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
        Assertions.assertThrows(RuntimeException.class, () -> {
            carRentalService.reserveCar(CarType.SEDAN, startDate.plusDays(2), 2);
        });
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
}