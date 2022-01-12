package com.example.appointment_booking.work.service;

import com.example.appointment_booking.work.model.WorkDto;
import com.example.appointment_booking.work.persistence.entity.Work;

public interface WorkService {
    void createWork(WorkDto workDtoDto);

    void updateWork(WorkDto workDto);

    void deleteWork(Long id);

    Work getWorkById(Long id);
}
