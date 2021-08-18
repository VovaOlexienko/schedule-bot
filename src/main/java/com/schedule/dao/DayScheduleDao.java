package com.schedule.dao;

import com.schedule.modal.DaySchedule;
import com.schedule.modal.StudentGroup;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface DayScheduleDao extends Dao<DaySchedule> {

    List<DaySchedule> saveAll(List<DaySchedule> objs);

    void deleteAll();

    Optional<DaySchedule> getByDayAndStudentGroup(StudentGroup studentGroup, DayOfWeek dayOfWeek);
}
