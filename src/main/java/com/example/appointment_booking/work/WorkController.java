package com.example.appointment_booking.work;

import com.example.appointment_booking.work.model.WorkDto;
import com.example.appointment_booking.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
@CrossOrigin(origins = "http://localhost:3000")
public class WorkController {
    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService) {
        this.workService = workService;
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    void createWork(@RequestBody WorkDto workDto) {
        workService.createWork(workDto);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateWork(@RequestBody WorkDto workDto) {
        workService.updateWork(workDto);
    }

    @DeleteMapping("/{id}")
    void deleteWork(@PathVariable Long id) {
        workService.deleteWork(id);
    }
}
