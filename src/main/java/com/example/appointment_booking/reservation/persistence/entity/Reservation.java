package com.example.appointment_booking.reservation.persistence.entity;

import com.example.appointment_booking.work.persistence.entity.Work;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reservation_id")
    private Long id;
    private String name;
    private String phone;
    private String email;

    @ManyToMany
    @MapKeyColumn(name="service_dates", columnDefinition = "TIMESTAMP")
    private Map<LocalDateTime, Work> services = new HashMap<>();


    public Reservation(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

   public Reservation(String name, String phone, String email, Map<LocalDateTime,Work> services) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.services = services;
    }

    public Reservation(String name, String phone, String email, Work service, LocalDateTime date) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.services.put(date,service);
    }

}