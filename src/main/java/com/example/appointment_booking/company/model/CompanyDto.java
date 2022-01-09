package com.example.appointment_booking.company.model;

import com.example.appointment_booking.company.persistence.entity.BusinessHours;
import com.example.appointment_booking.company.persistence.entity.BusinessHoursConverter;
import com.example.appointment_booking.service.persistence.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String address;
    private String businessHours;
    private List<Service> services;

    private final static BusinessHoursConverter businessHoursConverter= new BusinessHoursConverter();

    public CompanyDto(String name, String address, String businessHours) {
        this.id = -1L;
        this.name = name;
        this.address = address;
        this.businessHours = businessHours;
        this.services = new ArrayList<>();
    }

    public CompanyDto(Long id,String name, String address, String businessHours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.businessHours = businessHours;
        this.services = new ArrayList<>();
    }

    public Set<BusinessHours> getBusinessHoursAsSet(){
        return businessHoursConverter.convertToEntityAttribute(businessHours);
    }
}
