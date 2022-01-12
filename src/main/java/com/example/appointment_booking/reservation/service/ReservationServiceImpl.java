package com.example.appointment_booking.reservation.service;

import com.example.appointment_booking.company.persistence.entity.BusinessHours;
import com.example.appointment_booking.company.persistence.entity.Company;
import com.example.appointment_booking.exception.CustomException;
import com.example.appointment_booking.reservation.model.ReservationDto;
import com.example.appointment_booking.reservation.model.ReservationHelper;
import com.example.appointment_booking.reservation.persistence.entity.Reservation;
import com.example.appointment_booking.reservation.persistence.repository.ReservationRepository;
import com.example.appointment_booking.work.persistence.entity.Work;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final static int BREAK_TIME_IN_MIN = 5;

    private ReservationRepository reservationRepository;
    private ReservationHelper reservationHelper;


    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationHelper reservationHelper) {
        this.reservationRepository = reservationRepository;
        this.reservationHelper = reservationHelper;
    }

    @Override
    public void createReservation(ReservationDto reservationDto) {
        if(isValidReservation(reservationDto)){
           reservationRepository.save(convertDtoToEntity(reservationDto));
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

    boolean isValidReservation(ReservationDto reservationDto) {
        return areRequiredFieldsNonNull(reservationDto)
                && isValidEmail(reservationDto.getEmail())
                && isValidPhoneNumber(reservationDto.getPhone())
                && isReservationFromOneCompany(reservationDto)
                && isValidAppointment(reservationDto)
                && isAvailableAppointment(reservationDto);

    }

    boolean areRequiredFieldsNonNull(ReservationDto reservationDto) {
        if (reservationDto == null || reservationDto.getName() == null || reservationDto.getPhone() == null
                || reservationDto.getEmail() == null) {
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
        //TODO
        return true;
    }

    boolean isReservationFromOneCompany(ReservationDto reservationDto) {
        boolean oneCompany = convertDtoToEntity(reservationDto)
                .getServices()
                .values()
                .stream()
                .map(Work::getId)
                .distinct().count() == 1;
        if (!oneCompany) {
            throw new CustomException("All reservations must be from one company", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return true;
    }

    boolean isValidAppointment(ReservationDto reservationDto) {
        for (Map.Entry<LocalDateTime, Work> entry : convertDtoToEntity(reservationDto).getServices().entrySet()) {
            LocalDateTime dateStart = entry.getKey();
            LocalDateTime dateEnd = dateStart.plusMinutes(entry.getValue().getDuration());
            Set<BusinessHours> businessHours = entry.getValue().getCompany().getBusinessHours();

            if (dateStart.isBefore(LocalDateTime.now())) {
                throw new CustomException("Invalid date", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (dateStart.getDayOfYear()!=dateEnd.getDayOfYear()){
                throw new CustomException("Reservation's start and end must be on the same day", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if(dateStart.getMinute()!=0 && dateStart.getMinute()!=30){
                throw new CustomException("Reservation's start minute must be 0 or 30", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if(!BusinessHours.isInOpening(businessHours, dateStart) || !BusinessHours.isInOpening(businessHours, dateEnd)){
                throw new CustomException("The company is not open at the selected time", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        return true;
    }


    boolean isAvailableAppointment(ReservationDto reservationDto) {
        List<Work> companyServices = getCompanyFromReservedService(reservationDto).getWorks();
        for (Map.Entry<LocalDateTime, Work> entry : convertDtoToEntity(reservationDto).getServices().entrySet()) {
            LocalDateTime searchedDateStart = entry.getKey();
            int duration = entry.getValue().getDuration() + BREAK_TIME_IN_MIN;
            LocalDateTime searchedDateEnd = searchedDateStart.plusMinutes(duration);

            isOverlappingDate(companyServices, searchedDateStart, searchedDateEnd);
        }
        return true;
    }

    private void isOverlappingDate(List<Work> companyServices, LocalDateTime searchedDateStart, LocalDateTime searchedDateEnd) {
        for (Work service : companyServices) {
            int duration = service.getDuration() + BREAK_TIME_IN_MIN;
            List<LocalDateTime> reservedDates = getReservedDatesForService(service);
            for (LocalDateTime reservedDateStart : reservedDates) {
                if (reservedDateStart.isAfter(searchedDateEnd)) {
                    break;
                }
                LocalDateTime reservedDateEnd = reservedDateStart.plusMinutes(duration);
                if (searchedDateStart.isBefore(reservedDateEnd) && reservedDateStart.isBefore(searchedDateEnd)) {
                    throw new CustomException("Overlapping reservations", HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }
    }

    private List<LocalDateTime> getReservedDatesForService(Work service){
        return service.getReservations().stream()
                .map(r -> r.getServices().entrySet())
                .flatMap(Collection::stream)
                .filter(e -> e.getValue().equals(service))
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    Company getCompanyFromReservedService(ReservationDto reservationDto) {
        if (!isReservationFromOneCompany(reservationDto)) {
            throw new CustomException("All reservations must be from one company", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return convertDtoToEntity(reservationDto).getServices().values().stream()
                .map(Work::getCompany)
                .findFirst()
                .orElseThrow(() -> new CustomException("You must choose a service", HttpStatus.UNPROCESSABLE_ENTITY));
    }

    private ReservationDto convertEntityToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .phone(reservation.getPhone())
                .email(reservation.getEmail())
                .services(reservationHelper.convertServicesMapToString(reservation.getServices()))
                .build();
    }

    private Reservation convertDtoToEntity(ReservationDto reservationDto) {
        return Reservation.builder()
                .id(reservationDto.getId())
                .name(reservationDto.getName())
                .phone(reservationDto.getPhone())
                .email(reservationDto.getEmail())
                .services(reservationHelper.convertServicesStringToMap(reservationDto.getServices()))
                .build();
    }
}
