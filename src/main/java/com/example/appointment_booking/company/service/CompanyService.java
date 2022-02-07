package com.example.appointment_booking.company.service;

import com.example.appointment_booking.company.model.CompanyDto;
import com.example.appointment_booking.company.persistence.entity.Company;

import java.util.List;

public interface CompanyService {

    void createCompany(CompanyDto companyDto);

    void updateCompany(CompanyDto companyDto);

    void deleteCompany(Long id);

    List<Company> getAllCompanies();

    Company getCompanyById(Long id);
}
