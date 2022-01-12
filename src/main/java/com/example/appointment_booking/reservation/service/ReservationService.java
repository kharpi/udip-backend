package com.example.appointment_booking.reservation.service;

import com.example.appointment_booking.reservation.model.ReservationDto;

import java.util.List;

public interface ReservationService {

    void createReservation(ReservationDto reservationDto);

    List<ReservationDto> getReservations();

    void deleteReservation(Long id);
}
