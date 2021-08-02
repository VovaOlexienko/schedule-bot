package com.schedule.repository;

import com.schedule.modal.DaySchedule;
import com.schedule.modal.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {
    DaySchedule findByStudentGroupAndDayOfWeek(StudentGroup studentGroup, DayOfWeek dayOfWeek);
}
