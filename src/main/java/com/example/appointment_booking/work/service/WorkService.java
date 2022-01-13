package com.example.appointment_booking.work.service;

import com.example.appointment_booking.work.model.WorkDto;
import com.example.appointment_booking.work.persistence.entity.Work;

import java.util.List;

public interface WorkService {
    void createWork(WorkDto workDtoDto);

    void updateWork(WorkDto workDto);

    void deleteWork(Long id);

    int getDurationForServices(List<Work> services);

    List<Work> getServicesByIDs(List<Long> ids);

    Work getWorkById(Long id);
}
