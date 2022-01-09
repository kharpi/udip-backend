package com.example.appointment_booking.company.service;

import com.example.appointment_booking.company.persistence.entity.Company;
import com.example.appointment_booking.company.model.CompanyDto;
import com.example.appointment_booking.company.persistence.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        if(isValidCompany(companyDto)){
            companyRepository.save(convertDtoToEntity(companyDto));
        }
        //Todo throw custom exception otherwise
    }

    @Override
    public void updateCompany(CompanyDto companyDto) {
        if(isValidCompany(companyDto) && companyRepository.existsById(companyDto.getId())){
            companyRepository.save(convertDtoToEntity(companyDto));
        }
        //Todo throw custom exception otherwise
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(); //Todo
        if(company.getServices().isEmpty()){
            companyRepository.delete(company);
        }
        //Todo throw custom exception otherwise
    }

    private boolean isValidCompany(CompanyDto companyDto){
        if(companyDto==null ||companyDto.getName()==null|| companyDto.getAddress() == null
                || companyDto.getBusinessHours()==null){
            return false;
        }
        return true;
    }

    private Company convertDtoToEntity(CompanyDto companyDto){
        return Company.builder()
                .id(companyDto.getId())
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .businessHours(companyDto.getBusinessHoursAsSet())
                .services(companyDto.getServices())
                .build();
    }


}
