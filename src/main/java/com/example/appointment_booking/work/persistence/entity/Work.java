package com.example.appointment_booking.work.persistence.entity;

import com.example.appointment_booking.company.persistence.entity.Company;
import com.example.appointment_booking.reservation.persistence.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_id")
    private Long id;

    private String name;

    private String description;

    private Integer duration;


    @ManyToOne
    @JoinTable(
            name = "company_service",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private Company company;

    @ManyToMany(mappedBy = "works")
    private List<Reservation> reservations;

    public Work(String name, String description, Integer duration, Company company) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.company = company;
    }
}
