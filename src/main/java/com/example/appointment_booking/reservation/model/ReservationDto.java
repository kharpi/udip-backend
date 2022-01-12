package com.example.appointment_booking.reservation.model;

import lombok.*;


@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String services;


    public ReservationDto(String name, String phone, String email, String services) {
        this.id = -1L;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.services = services;
    }

    public ReservationDto(Long id, String name, String phone, String email, String services) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.services = services;
    }
}
