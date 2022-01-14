package com.example.appointment_booking.config;

import com.example.appointment_booking.company.persistence.entity.BusinessHours;
import com.example.appointment_booking.company.persistence.entity.Company;
import com.example.appointment_booking.company.persistence.repository.CompanyRepository;
import com.example.appointment_booking.reservation.persistence.entity.Reservation;
import com.example.appointment_booking.reservation.persistence.repository.ReservationRepository;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.persistence.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

@Component
public class DatabaseInitializer {

    private final CompanyRepository  companyRepository;

    private final WorkRepository workRepository;

    private final ReservationRepository reservationRepository;

    @Autowired
    public DatabaseInitializer(CompanyRepository companyRepository, WorkRepository workRepository, ReservationRepository reservationRepository) {
        this.companyRepository = companyRepository;
        this.workRepository = workRepository;
        this.reservationRepository = reservationRepository;
    }

    @PostConstruct
    public void init() {
        HashSet<BusinessHours> businessHoursSet = new HashSet<>(List.of(
                new BusinessHours(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0))
                , new BusinessHours(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0))
                , new BusinessHours(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0))
                , new BusinessHours(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(16, 0))
                , new BusinessHours(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(14, 0))
                , new BusinessHours(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(13, 0))));


        Company company = new Company("sampleCompany", "sample street", businessHoursSet);
        companyRepository.save(company);

        Work work1 = new Work("sampleService1", "Description of sampleService1", 60, company);
        Work work2 = new Work("sampleService2", "Description of sampleService2", 40, company);
        workRepository.save(work1);
        workRepository.save(work2);

        LocalDateTime date = LocalDateTime.now().withMinute(30).truncatedTo(ChronoUnit.MINUTES);
        Reservation reservation = new Reservation("Joe", "3630765243", "email@email.com", date, 105, List.of(work1, work2));

        reservationRepository.save(reservation);

    }
}
