package com.example.appointment_booking.reservation.persistence.entity;

import com.example.appointment_booking.work.persistence.entity.Work;
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

    private LocalDateTime date;
    private Integer duration;

    @ManyToMany
    List<Work> works = new ArrayList<>();


    public Reservation(String name, String phone, String email, LocalDateTime date, int duration, List<Work> works) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.duration = duration;
        this.works = works;
    }

}