package com.schedule.dao.impl;

import com.schedule.dao.DayScheduleDao;
import com.schedule.modal.DaySchedule;
import com.schedule.modal.StudentGroup;
import com.schedule.repository.DayScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DayScheduleService implements DayScheduleDao {

    @Autowired
    DayScheduleRepository dayScheduleRepository;

    @Override
    public Optional<DaySchedule> get(Long id) {
        return Optional.ofNullable(dayScheduleRepository.getById(id));
    }

    @Override
    public DaySchedule save(DaySchedule obj) {
        return dayScheduleRepository.save(obj);
    }

    @Override
    public void delete(DaySchedule obj) {
        dayScheduleRepository.delete(obj);
    }

    @Override
    public List<DaySchedule> getAll() {
        return dayScheduleRepository.findAll();
    }

    @Override
    public List<DaySchedule> saveAll(List<DaySchedule> objs) {
        return dayScheduleRepository.saveAll(objs);
    }

    @Override
    public void deleteAll() {
        dayScheduleRepository.deleteAll();
    }

    @Override
    public Optional<DaySchedule> getByDayAndStudentGroup(StudentGroup studentGroup, DayOfWeek dayOfWeek) {
        return Optional.ofNullable(dayScheduleRepository.findByStudentGroupAndDayOfWeek(studentGroup, dayOfWeek));
    }
}
