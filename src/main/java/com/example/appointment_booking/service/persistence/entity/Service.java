package com.example.appointment_booking.service.persistence.entity;

import com.example.appointment_booking.company.persistence.entity.Company;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private Integer duration;

    @ManyToOne()
    @JoinColumn(name = "company_name")
    private Company company;

    public Service(String name, String description, Integer duration, Company company) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.company = company;
    }
}
