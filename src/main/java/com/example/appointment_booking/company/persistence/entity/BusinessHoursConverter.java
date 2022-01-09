package com.example.appointment_booking.company.persistence.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Converter
public class BusinessHoursConverter implements AttributeConverter<Set<BusinessHours>, String> {

    @Override
    public String convertToDatabaseColumn(Set<BusinessHours> businessHoursSet) {
        StringBuilder str = new StringBuilder();
        for (BusinessHours hours : businessHoursSet) {
            str.append(hours.getDay().name())
                    .append(":")
                    .append(hours.getFrom())
                    .append("-")
                    .append(hours.getTo())
                    .append(",");
        }
        return !"".equals(str.toString()) ? str.substring(0, str.length() - 1) : "";
    }

    @Override
    public Set<BusinessHours> convertToEntityAttribute(String s) {
        Set<BusinessHours> result = new HashSet<>();
        if ("".equals(s)) {
            return result;
        }

        String[] businessHours = s.split(",");
        for (String hours : businessHours) {
            String[] hoursArray = hours.split(":");
            DayOfWeek day = DayOfWeek.valueOf(hoursArray[0].toUpperCase());

            result.add(new BusinessHours(day, Integer.parseInt(hoursArray[1].split("-")[0])
                    , Integer.parseInt(hoursArray[1].split("-")[1])));
        }
        return result;
    }
}
