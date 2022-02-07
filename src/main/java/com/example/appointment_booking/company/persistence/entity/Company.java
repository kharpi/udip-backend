package com.example.appointment_booking.company.persistence.entity;

import com.example.appointment_booking.work.persistence.entity.Work;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String address;

    @Convert(converter = BusinessHoursConverter.class)
    @Column(name = "business_hours")
    private Set<BusinessHours> businessHours;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Work> works;

    public Company(String name, String address, HashSet<BusinessHours> businessHours) {
        this.name = name;
        this.address = address;
        this.businessHours = businessHours;
    }
}
