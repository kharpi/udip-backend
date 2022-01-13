package com.example.appointment_booking.reservation.service;

import com.example.appointment_booking.reservation.model.ReservationDto;
import com.example.appointment_booking.work.persistence.entity.Work;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    void createReservation(ReservationDto reservationDto);

    List<ReservationDto> getReservations();

    List<String> getValidDates(List<Work> services,LocalDateTime dateFrom, LocalDateTime dateTo, int max);

    void deleteReservation(Long id);
}
