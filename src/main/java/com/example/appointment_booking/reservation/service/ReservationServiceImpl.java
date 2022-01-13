package com.example.appointment_booking.reservation.service;

import com.example.appointment_booking.DateConverter;
import com.example.appointment_booking.company.persistence.entity.BusinessHours;
import com.example.appointment_booking.company.persistence.entity.Company;
import com.example.appointment_booking.exception.CustomException;
import com.example.appointment_booking.reservation.model.ReservationDto;
import com.example.appointment_booking.reservation.model.ReservationHelper;
import com.example.appointment_booking.reservation.persistence.entity.Reservation;
import com.example.appointment_booking.reservation.persistence.repository.ReservationRepository;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.service.WorkService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository reservationRepository;
    private final ReservationHelper reservationHelper;
    private final WorkService workService;


    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationHelper reservationHelper, WorkService workService) {
        this.reservationRepository = reservationRepository;
        this.reservationHelper = reservationHelper;
        this.workService = workService;
    }

    @Override
    public void createReservation(ReservationDto reservationDto) {
        Reservation reservation = convertDtoToEntity(reservationDto);
        reservation.setDuration(workService.getDurationForServices(reservation.getServices()));

        if (isValidReservation(reservation)) {
            reservationRepository.save(reservation);
        }
    }

    @Override
    public List<ReservationDto> getReservations() {
        return reservationRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new CustomException("The specified reservation cannot be found", HttpStatus.NOT_FOUND);
        }
        reservationRepository.deleteById(id);
    }

    boolean isValidReservation(Reservation reservation) {
        Company company = getCompanyFromServices(reservation.getServices());
        LocalDateTime start = reservation.getDate();
        LocalDateTime end = start.plusMinutes(reservation.getDuration());

        return areRequiredFieldsNonNull(reservation)
                && isValidEmail(reservation.getEmail())
                && isValidPhoneNumber(reservation.getPhone())
                && isValidAppointment(company,start,end)
                && isAvailableAppointment(company,start,end);

    }

    boolean areRequiredFieldsNonNull(Reservation reservation) { //TODO
        if (reservation == null || reservation.getName() == null || reservation.getPhone() == null
                || reservation.getEmail() == null) {
            throw new CustomException("Required fields cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return true;
    }

    boolean isValidEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new CustomException("Invalid email", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return true;
    }

    boolean isValidPhoneNumber(String phone) {
        String regex = "(\\+?)[0-9]{11}";
        if (!phone.matches(regex)) {
            throw new CustomException("Invalid phone number", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return true;
    }

    Company getCompanyFromServices(List<Work> services) {
        List<Company> companies = services
                .stream()
                .map(Work::getCompany)
                .distinct()
                .collect(Collectors.toList());
        if (companies.size() > 1) {
            throw new CustomException("All reservations must be from one company", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return companies.get(0);
    }

    private boolean isValidAppointment(Company company, LocalDateTime start, LocalDateTime end) {
        Set<BusinessHours> businessHours = company.getBusinessHours();
        if (start.isBefore(LocalDateTime.now())) {
            throw new CustomException("Invalid date", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (start.getMinute() != 0 && start.getMinute() != 30) {
            throw new CustomException("Reservation's start minute must be 0 or 30", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!BusinessHours.isInOpening(businessHours, start) || !BusinessHours.isInOpening(businessHours, end)) {
            throw new CustomException("The company is not open at the selected time", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (start.getDayOfYear() != end.getDayOfYear()) {
            throw new CustomException("Reservation's start and end must be on the same day", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return true;
    }

    @Override
    public List<String> getValidDates(List<Work> services,LocalDateTime dateFrom, LocalDateTime dateTo, int max) {
        List<String> availableDates = new ArrayList<>();

        int duration = workService.getDurationForServices(services);
        Company company = getCompanyFromServices(services);
        List<DayOfWeek> openDays = company.getBusinessHours().stream().map(BusinessHours::getDay).collect(Collectors.toList());

        LocalDateTime date = dateFrom;
        while (date.isBefore(dateTo) && availableDates.size() < max) {

            LocalDateTime dateEnd = date.plusMinutes(duration);
            if(!openDays.contains(date.getDayOfWeek())){
                date = date.plusDays(1).withHour(0).withMinute(0);
            }
            try {
                isValidAppointment(company,date, dateEnd);
                isAvailableAppointment(company, date, dateEnd);
                availableDates.add(DateConverter.convertDateToString(date));
            } catch (CustomException ignored) {}
            finally {
                date = date.plusMinutes(30);
            }
        }
        return availableDates;
    }

    private boolean isAvailableAppointment(Company company, LocalDateTime searchedStart, LocalDateTime searchedEnd) {
        for(Reservation res : getReservationsForCompany(company)){
            LocalDateTime reservedStart = res.getDate();
            LocalDateTime reservedEnd = reservedStart.plusMinutes(res.getDuration());
            if (reservedStart.isAfter(searchedEnd)) {
                break;
            }
            if (searchedStart.isBefore(reservedEnd) && reservedStart.isBefore(searchedEnd)) {
                throw new CustomException("Overlapping reservations", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        return true;
    }

    private List<Reservation> getReservationsForCompany(Company company) {
        return company.getWorks().stream()
                .map(Work::getReservations)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Reservation::getDate))
                .collect(Collectors.toList());

    }

    private ReservationDto convertEntityToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .phone(reservation.getPhone())
                .email(reservation.getEmail())
                .date(DateConverter.convertDateToString(reservation.getDate()))
                .services(reservationHelper.convertServicesListToString(reservation.getServices()))
                .build();
    }

    private Reservation convertDtoToEntity(ReservationDto reservationDto) {
        return Reservation.builder()
                .id(reservationDto.getId())
                .name(reservationDto.getName())
                .phone(reservationDto.getPhone())
                .email(reservationDto.getEmail())
                .date(DateConverter.convertStringToDate(reservationDto.getDate()))
                .services(reservationHelper.convertServicesStringToList(reservationDto.getServices()))
                .build();
    }
}
