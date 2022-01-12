package com.example.appointment_booking.company.persistence.entity;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

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

    public static boolean isInOpening(Set<BusinessHours> businessHours,LocalDateTime date){
        for(BusinessHours businessHour: businessHours){
            if(businessHour.isOpen(date)){
                return true;
            }
        }
        return false;
    }

    public boolean isOpen(LocalDateTime date) {

        if (day != date.getDayOfWeek()) {
            return false;
        }

        String f = String.format("%04d", from);
        String t = String.format("%04d", to);

        int fromHour = Integer.parseInt(f.substring(0, 2));
        int fromMinute = Integer.parseInt(f.substring(2));

        int toHour = Integer.parseInt(t.substring(0, 2));
        int toMinute = Integer.parseInt(t.substring(2));

        LocalDateTime intStart = date.withHour(fromHour).withMinute(fromMinute);
        LocalDateTime intEnd = date.withHour(toHour).withMinute(toMinute);

        if (intStart.equals(date)) {
            return true;
        }
        return intStart.isBefore(date) && intEnd.isAfter(date);

    }
}
