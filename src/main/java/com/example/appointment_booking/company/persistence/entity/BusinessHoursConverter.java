package com.example.appointment_booking.company.persistence.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Converter
public class BusinessHoursConverter implements AttributeConverter<Set<BusinessHours>, String> {

    private static final String SEPARATOR_AFTER_DAY = ":";
    private static final String SEPARATOR_BETWEEN_HOURS = "-";
    private static final String SEPARATOR_BETWEEN_ENTRIES = ",";


    @Override
    public String convertToDatabaseColumn(Set<BusinessHours> businessHoursSet) {
        StringBuilder str = new StringBuilder();
        for (BusinessHours hours : businessHoursSet) {
            str.append(hours.getDay().name())
                    .append(SEPARATOR_AFTER_DAY)
                    .append(hours.getFrom())
                    .append(SEPARATOR_BETWEEN_HOURS)
                    .append(hours.getTo())
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

            result.add(new BusinessHours(day, Integer.parseInt(hoursArray[1].split(SEPARATOR_BETWEEN_HOURS)[0])
                    , Integer.parseInt(hoursArray[1].split(SEPARATOR_BETWEEN_HOURS)[1])));
        }
        return result;
    }
}
