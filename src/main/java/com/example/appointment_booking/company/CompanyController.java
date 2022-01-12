package com.example.appointment_booking.company;

import com.example.appointment_booking.company.model.CompanyDto;
import com.example.appointment_booking.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    void createCompany(@RequestBody CompanyDto companyDto) {
        companyService.createCompany(companyDto);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateCompany(@RequestBody CompanyDto companyDto) {
        companyService.updateCompany(companyDto);
    }

    @DeleteMapping("/{id}")
    void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }

}
