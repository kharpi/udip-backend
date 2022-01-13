package com.example.appointment_booking.company.service;

import com.example.appointment_booking.company.model.CompanyDto;

import java.util.List;

public interface CompanyService {

    void createCompany(CompanyDto companyDto);

    void updateCompany(CompanyDto companyDto);

    void deleteCompany(Long id);

    List<CompanyDto> getAllCompanies();
}
