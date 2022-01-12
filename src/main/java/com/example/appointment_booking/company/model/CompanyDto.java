package com.example.appointment_booking.company.model;

import com.example.appointment_booking.company.persistence.entity.BusinessHours;
import com.example.appointment_booking.company.persistence.entity.BusinessHoursConverter;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    private List<Work> works;

    private final static BusinessHoursConverter businessHoursConverter= new BusinessHoursConverter();

    public CompanyDto(String name, String address, String businessHours) {
        this.id = -1L;
        this.name = name;
        this.address = address;
        this.businessHours = businessHours;
        this.works = new ArrayList<>();
    }

    public CompanyDto(Long id,String name, String address, String businessHours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.businessHours = businessHours;
        this.works = new ArrayList<>();
    }

    public Set<BusinessHours> getBusinessHoursAsSet(){
        return businessHoursConverter.convertToEntityAttribute(businessHours);
    }
}
