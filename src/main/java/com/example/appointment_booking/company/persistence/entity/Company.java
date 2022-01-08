package com.example.appointment_booking.company.persistence.entity;

import com.example.appointment_booking.service.persistence.entity.Service;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Company {
    @Id
    private String name;

    private String address;

    @Convert(converter = BusinessHoursConverter.class)
    @Column(name = "business_hours")
    private Set<BusinessHours> businessHours;

    @OneToMany(mappedBy = "company")
    private List<Service> services;

    public Company(String name, String address, HashSet<BusinessHours> businessHours) {
        this.name = name;
        this.address = address;
        this.businessHours = businessHours;
    }
}
