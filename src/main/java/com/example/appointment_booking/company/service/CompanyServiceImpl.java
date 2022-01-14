package com.example.appointment_booking.company.service;

import com.example.appointment_booking.company.persistence.entity.Company;
import com.example.appointment_booking.company.model.CompanyDto;
import com.example.appointment_booking.company.persistence.repository.CompanyRepository;
import com.example.appointment_booking.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void createCompany(CompanyDto companyDto) {
        if (!isValidCompany(companyDto)) {
            throw new CustomException("Required fields cannot be null or empty", HttpStatus.UNPROCESSABLE_ENTITY);

        }
        companyRepository.save(convertDtoToEntity(companyDto));
    }

    @Override
    public void updateCompany(CompanyDto companyDto) {
        if (!companyRepository.existsById(companyDto.getId())) {
            throw new CustomException("The specified company cannot be found", HttpStatus.NOT_FOUND);
        }
        if (!isValidCompany(companyDto)) {
            throw new CustomException("Required fields cannot be null or empty", HttpStatus.NOT_FOUND);
        }
        companyRepository.save(convertDtoToEntity(companyDto));
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException("The specified company cannot be found", HttpStatus.NOT_FOUND));
        if (!company.getWorks().isEmpty()) {
            throw new CustomException("Cannot delete company while there are services linked to it", HttpStatus.CONFLICT);
        }
        companyRepository.delete(company);
    }

    private boolean isValidCompany(CompanyDto companyDto) {
        return companyDto != null && companyDto.getName() != null && !"".equals(companyDto.getName())
                && companyDto.getAddress() != null && !"".equals(companyDto.getAddress())
                && companyDto.getBusinessHours() != null && !"".equals(companyDto.getBusinessHours());
    }

    private Company convertDtoToEntity(CompanyDto companyDto) {
        return Company.builder()
                .id(companyDto.getId())
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .businessHours(companyDto.getBusinessHoursAsSet())
                .works(companyDto.getWorks())
                .build();
    }
}
