package com.example.appointment_booking.reservation.model;

import lombok.*;


@Builder
@Data
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String date;
    private String works;


    public ReservationDto(String name, String phone, String email, String date, String works) {
        this.id = -1L;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.works = works;
        this.date = date;
    }

    public ReservationDto(Long id, String name, String phone, String email, String date, String works) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.works = works;
    }
}
