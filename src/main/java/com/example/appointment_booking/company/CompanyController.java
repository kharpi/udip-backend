package com.example.appointment_booking.company;

import com.example.appointment_booking.company.model.CompanyDto;
import com.example.appointment_booking.company.persistence.entity.Company;
import com.example.appointment_booking.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @GetMapping
    List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    Company getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @PostMapping("/create")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    void createCompany(@RequestBody CompanyDto companyDto) {
        companyService.createCompany(companyDto);
    }

    @PostMapping("/update")
    @CrossOrigin
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateCompany(@RequestBody CompanyDto companyDto) {
        companyService.updateCompany(companyDto);
    }


    @DeleteMapping("/{id}")
    @CrossOrigin
    void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }


}
