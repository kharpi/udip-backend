package com.example.appointment_booking.reservation.model;

import com.example.appointment_booking.DateConverter;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationHelper {

    private static final String SEPARATOR_BETWEEN_ENTRIES = ",";

    private final WorkService workService;

    @Autowired
    public ReservationHelper(WorkService workService) {
        this.workService = workService;
    }

    public String convertServicesListToString(List<Work> services) {
        StringBuilder sb = new StringBuilder();

        for (Work service : services) {
            sb.append(service.getId()).append(SEPARATOR_BETWEEN_ENTRIES);
        }
        return !"".equals(sb.toString()) ? sb.substring(0, sb.length() - 1) : "";
    }

    public List<Work> convertServicesStringToList(String services) {
        return Arrays.stream(services.split(SEPARATOR_BETWEEN_ENTRIES)).map(s -> workService.getWorkById(Long.valueOf(s))).collect(Collectors.toList());
    }
}
