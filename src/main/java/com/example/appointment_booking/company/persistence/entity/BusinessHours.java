package com.example.appointment_booking.company.persistence.entity;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
public class BusinessHours {

    private final DayOfWeek day;
    private final Integer from;
    private final Integer to;

    public BusinessHours(DayOfWeek day, Integer from, Integer to) {
        this.day = day;
        this.from = from;
        this.to = to;
    }

    public boolean isOpen(LocalDateTime start) {

        if (day != start.getDayOfWeek()) {
            return false;
        }

        String f = String.format("%04d", from);
        String t = String.format("%04d", to);

        int fromHour = Integer.parseInt(f.substring(0, 2));
        int fromMinute = Integer.parseInt(f.substring(2));

        int toHour = Integer.parseInt(t.substring(0, 2));
        int toMinute = Integer.parseInt(t.substring(2));

        LocalDateTime intStart = start.withHour(fromHour).withMinute(fromMinute);
        LocalDateTime intEnd = start.withHour(toHour).withMinute(toMinute);

        if (intStart.equals(start)) {
            return true;
        }
        return intStart.isBefore(start) && intEnd.isAfter(start);

    }
}
