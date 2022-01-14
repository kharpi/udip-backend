package com.example.appointment_booking.reservation;

import com.example.appointment_booking.reservation.model.ReservationDto;
import com.example.appointment_booking.reservation.service.ReservationService;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final static int DEFAULT_LIMIT = 10;
    private final static int DEFAULT_INTERVAL_DAYS = 7;

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

    @GetMapping("/{workIds}")
    List<String> getAvailableDates(@PathVariable List<Long> workIds, @RequestParam Optional<String> from
            , @RequestParam Optional<String> to, @RequestParam Optional<Integer> max) {
        List<Work> works = workService.getWorksByIDs(workIds);

        LocalDateTime dateFrom = from.map(DateConverter::convertStringToDate)
                .orElseGet(LocalDateTime::now);

        LocalDateTime dateTo = to.map(DateConverter::convertStringToDate)
                .orElseGet(() -> dateFrom.plusDays(DEFAULT_INTERVAL_DAYS));

        int maxLimit = max.orElse(DEFAULT_LIMIT);

        return reservationService.getAvailableDates(works, dateFrom, dateTo, maxLimit);
    }

    @DeleteMapping("/{id}")
    void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }


}
