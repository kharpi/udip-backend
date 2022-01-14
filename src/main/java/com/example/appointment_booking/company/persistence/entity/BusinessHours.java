package com.example.appointment_booking.company.persistence.entity;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Getter
public class BusinessHours {

    private final DayOfWeek day;
    private final LocalTime from;
    private final LocalTime to;

    public BusinessHours(DayOfWeek day, LocalTime from, LocalTime to) {
        this.day = day;
        this.from = from;
        this.to = to;
    }

    public static boolean isInOpening(Set<BusinessHours> businessHours, LocalDateTime date) {
        for (BusinessHours businessHour : businessHours) {
            if (businessHour.isOpen(date)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOpen(LocalDateTime date) {

        if (day != date.getDayOfWeek()) {
            return false;
        }

        LocalDateTime dateStart = date.withHour(from.getHour()).withMinute(from.getMinute());
        LocalDateTime dateEnd = date.withHour(to.getHour()).withMinute(to.getMinute());

        if (dateStart.equals(date)) {
            return true;
        }
        return dateStart.isBefore(date) && dateEnd.plusSeconds(1L).isAfter(date);

    }
}
