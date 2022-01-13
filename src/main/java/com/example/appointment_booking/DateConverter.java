package com.example.appointment_booking;

import com.example.appointment_booking.exception.CustomException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConverter {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static LocalDateTime convertStringToDate(String str) {
        try {
            return LocalDateTime.parse(str, formatter);
        }
        catch (DateTimeParseException e){
            throw new CustomException("Invalid date format. The format must be\""+DATE_PATTERN+"\".", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public static String convertDateToString(LocalDateTime date) {
        return date.format(formatter);
    }
}
