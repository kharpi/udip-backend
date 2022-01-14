package com.example.appointment_booking.company.service;

import com.example.appointment_booking.company.model.CompanyDto;

public interface CompanyService {

    void createCompany(CompanyDto companyDto);

    void updateCompany(CompanyDto companyDto);

    void deleteCompany(Long id);
}
