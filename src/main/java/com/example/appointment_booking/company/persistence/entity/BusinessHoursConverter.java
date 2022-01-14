package com.example.appointment_booking.company.persistence.entity;

import com.example.appointment_booking.exception.CustomException;
import org.springframework.http.HttpStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

@Converter
public class BusinessHoursConverter implements AttributeConverter<Set<BusinessHours>, String> {

    private static final String SEPARATOR_AFTER_DAY = ":";
    private static final String SEPARATOR_BETWEEN_HOURS = "-";
    private static final String SEPARATOR_BETWEEN_ENTRIES = ",";
    private static final String TIME_PATTERN = "HHmm";

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    @Override
    public String convertToDatabaseColumn(Set<BusinessHours> businessHoursSet) {
        StringBuilder str = new StringBuilder();
        for (BusinessHours hours : businessHoursSet) {
            str.append(hours.getDay().name())
                    .append(SEPARATOR_AFTER_DAY)
                    .append(hours.getFrom().format(formatter))
                    .append(SEPARATOR_BETWEEN_HOURS)
                    .append(hours.getTo().format(formatter))
                    .append(SEPARATOR_BETWEEN_ENTRIES);
        }
        return !"".equals(str.toString()) ? str.substring(0, str.length() - 1) : "";
    }

    @Override
    public Set<BusinessHours> convertToEntityAttribute(String s) {
        Set<BusinessHours> result = new HashSet<>();
        if ("".equals(s)) {
            return result;
        }

        String[] businessHours = s.split(SEPARATOR_BETWEEN_ENTRIES);
        for (String hours : businessHours) {
            String[] hoursArray = hours.split(SEPARATOR_AFTER_DAY);
            DayOfWeek day = DayOfWeek.valueOf(hoursArray[0].toUpperCase());
            try {
                LocalTime from = LocalTime.parse(hoursArray[1].split(SEPARATOR_BETWEEN_HOURS)[0], formatter);
                LocalTime to = LocalTime.parse(hoursArray[1].split(SEPARATOR_BETWEEN_HOURS)[1], formatter);
                result.add(new BusinessHours(day, from, to));
            } catch (DateTimeParseException e) {
                throw new CustomException("Invalid hour form for businessHours. Correct pattern:\"" + TIME_PATTERN + "\"", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        return result;
    }
}
