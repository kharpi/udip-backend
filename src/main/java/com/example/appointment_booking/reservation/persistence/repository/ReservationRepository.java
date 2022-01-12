package com.example.appointment_booking.reservation.persistence.repository;

import com.example.appointment_booking.reservation.persistence.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
