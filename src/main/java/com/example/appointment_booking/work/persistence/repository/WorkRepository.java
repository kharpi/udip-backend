package com.example.appointment_booking.work.persistence.repository;

import com.example.appointment_booking.work.persistence.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {
}
