package com.example.appointment_booking.work.service;

import com.example.appointment_booking.company.persistence.repository.CompanyRepository;
import com.example.appointment_booking.exception.CustomException;
import com.example.appointment_booking.work.model.WorkDto;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.persistence.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final CompanyRepository companyRepository;

    private final static int BREAK_TIME_IN_MIN = 5;

    @Autowired
    public WorkServiceImpl(WorkRepository workRepository, CompanyRepository companyRepository) {

        this.workRepository = workRepository;
        this.companyRepository = companyRepository;
    }


    @Override
    public void createWork(WorkDto workDto) {
        if (!isValidWork(workDto)) {
            throw new CustomException("Required fields cannot be null", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        workRepository.save(convertDtoToEntity(workDto));
    }

    @Override
    public void updateWork(WorkDto workDto) {
        if (!workRepository.existsById(workDto.getId())) {
            throw new CustomException("The specified service cannot be found", HttpStatus.NOT_FOUND);
        }
        if (!isValidWork(workDto)) {
            throw new CustomException("Required fields cannot be null", HttpStatus.NOT_FOUND);
        }
        workRepository.save(convertDtoToEntity(workDto));
    }

    @Override
    public Work getWorkById(Long id) {
        return workRepository.getById(id);
    }

    @Override
    public void deleteWork(Long id) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new CustomException("The specified service cannot be found", HttpStatus.NOT_FOUND));
        if (!work.getReservations().isEmpty()) {
            throw new CustomException("Cannot delete service while there are reservations linked to it", HttpStatus.CONFLICT);
        }
        workRepository.delete(work);
    }

    @Override
    public int getDurationForServices(List<Work> services) {
        return services.stream().mapToInt(Work::getDuration).sum() + (BREAK_TIME_IN_MIN * (services.size() - 1));
    }

    @Override
    public List<Work> getServicesByIDs(List<Long> ids){ //TODO ha nem létezik az id
        return ids.stream().map(i -> workRepository.getById(i)).collect(Collectors.toList());
    }

    private boolean isValidWork(WorkDto workDto) {
        if (workDto == null || workDto.getName() == null || workDto.getDescription() == null
                || workDto.getDuration() == null) {
            return false;
        }
        return true;
    }

    private Work convertDtoToEntity(WorkDto workDto) {
        return Work.builder()
                .id(workDto.getId())
                .name(workDto.getName())
                .description(workDto.getDescription())
                .duration(workDto.getDuration())
                .company(companyRepository.getById(workDto.getCompanyId()))
                .build();
    }
}
