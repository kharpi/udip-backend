package com.example.appointment_booking.work.model;

import com.example.appointment_booking.reservation.persistence.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class WorkDto {

    private Long id;

    private String name;

    private String description;

    private Integer duration;

    private long companyId;

    private List<Reservation> reservations = new ArrayList<>();

    public WorkDto(String name, String description, Integer duration) {
        this.id = -1L;
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    public WorkDto(Long id, String name, String description, Integer duration, long companyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.companyId = companyId;
    }
}
