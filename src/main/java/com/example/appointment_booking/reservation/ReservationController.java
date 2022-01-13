package com.example.appointment_booking.reservation;

import com.example.appointment_booking.DateConverter;
import com.example.appointment_booking.reservation.model.ReservationDto;
import com.example.appointment_booking.reservation.service.ReservationService;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;
    private final WorkService workService;


    @Autowired
    public ReservationController(ReservationService reservationService, WorkService workService) {
        this.reservationService = reservationService;
        this.workService = workService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    void createReservation(@RequestBody ReservationDto reservationDto) {
        reservationService.createReservation(reservationDto);
    }

   @GetMapping("/{serviceIds}")
    List<String> getAvailableDates(@PathVariable List<Long> serviceIds,@RequestParam(required = false) String from
            ,@RequestParam(required = false) String to, @RequestParam(required = false) int max) {

        List<Work> services = workService.getServicesByIDs(serviceIds);
        LocalDateTime dateFrom = DateConverter.convertStringToDate(from);
        LocalDateTime dateTo = DateConverter.convertStringToDate(to);

        return reservationService.getValidDates(services,dateFrom,dateTo,max);
    }

    @DeleteMapping("/{id}")
    void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }





}
