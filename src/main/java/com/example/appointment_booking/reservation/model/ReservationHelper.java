package com.example.appointment_booking.reservation.model;

import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReservationHelper {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    private static final String SEPARATOR_AFTER_DATE = ";";
    private static final String SEPARATOR_BETWEEN_ENTRIES = ",";

    private final WorkService workService;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Autowired
    public ReservationHelper(WorkService workService) {
        this.workService = workService;
    }

    public String convertServicesMapToString(Map<LocalDateTime, Work> services) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<LocalDateTime, Work> entry : services.entrySet()) {
            sb.append(entry.getKey().format(formatter))
                    .append(SEPARATOR_AFTER_DATE)
                    .append(entry.getValue().getId())
                    .append(SEPARATOR_BETWEEN_ENTRIES);
        }
        return !"".equals(sb.toString()) ? sb.substring(0, sb.length() - 1) : "";
    }

    public Map<LocalDateTime, Work> convertServicesStringToMap(String services) {
        String[] entries = services.split(SEPARATOR_BETWEEN_ENTRIES);
        Map<LocalDateTime, Work> result = new HashMap<>();
        for (String entry : entries){
            String[] currentPair = entry.split(SEPARATOR_AFTER_DATE);
            LocalDateTime date = LocalDateTime.parse(currentPair[0], formatter);
            Work work = workService.getWorkById(Long.valueOf(currentPair[1]));
            result.put(date, work);
        }
        return result;
    }
}
