package com.carrental.repository;

import com.carrental.model.CarType;
import com.carrental.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
        select r.car.id from Reservation r
        where r.car.type = :type
          and r.startDateTime < :end
          and r.endDateTime > :start
    """)
    List<Long> findReservedCarIds(
            @Param("type") CarType type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
