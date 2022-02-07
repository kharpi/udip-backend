package com.example.appointment_booking.work;

import com.example.appointment_booking.work.model.WorkDto;
import com.example.appointment_booking.work.persistence.entity.Work;
import com.example.appointment_booking.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/service")
public class WorkController {
    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService) {
        this.workService = workService;
    }

    @GetMapping
    List<Work> getAllWork() {
        return workService.getAllWorks();
    }

    @GetMapping("/{id}")
    Work getWorkById(@PathVariable Long id) {
        return workService.getWorkById(id);
    }

    @PostMapping("/create")
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    void createWork(@RequestBody WorkDto workDto) {
        workService.createWork(workDto);
    }

    @PostMapping("/update")
    @CrossOrigin
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateWork(@RequestBody WorkDto workDto) {
        workService.updateWork(workDto);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin
    void deleteWork(@PathVariable Long id) {
        workService.deleteWork(id);
    }
}
