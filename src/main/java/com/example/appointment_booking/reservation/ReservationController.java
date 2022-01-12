package com.example.appointment_booking.reservation;

import com.example.appointment_booking.reservation.model.ReservationDto;
import com.example.appointment_booking.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    void createReservation(@RequestBody ReservationDto reservationDto){
        reservationService.createReservation(reservationDto);
    }

    @DeleteMapping("/{id}")
    void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }


}
