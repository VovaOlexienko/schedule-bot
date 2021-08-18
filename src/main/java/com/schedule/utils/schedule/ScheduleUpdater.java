package com.schedule.utils.schedule;

import com.schedule.dao.DayScheduleDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.DaySchedule;
import com.schedule.modal.StudentGroup;

import java.util.List;

public class ScheduleUpdater {

    public void updateSchedule(StudentGroupDao studentGroupDao, DayScheduleDao dayScheduleDao, String scheduleFilename) throws Exception {
        ExcelParser excelParser = new ExcelParser();
        excelParser.parseGroupsAndSchedulesToList(scheduleFilename);
        saveStudentGroupsAndSchedulesToDatabase(studentGroupDao, dayScheduleDao, excelParser.getGroups(), excelParser.getSchedules());
    }

    public void saveStudentGroupsAndSchedulesToDatabase(StudentGroupDao studentGroupDao,
                                                        DayScheduleDao dayScheduleDao,
                                                        List<StudentGroup> groups,
                                                        List<DaySchedule> schedules) {
        boolean b = true;
        dayScheduleDao.deleteAll();
        List<StudentGroup> studentGroups = studentGroupDao.getAll();
        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < studentGroups.size(); j++) {
                if (studentGroups.get(j).getNumber().equals(groups.get(i).getNumber())) {
                    b = false;
                    break;
                }
            }
            if (b) {
                studentGroupDao.save(groups.get(i));
            }
            b = true;
        }
        studentGroups = studentGroupDao.getAll();
        for (int i = 0; i < schedules.size(); i++) {
            for (StudentGroup studentGroup : studentGroups) {
                if (schedules.get(i).getStudentGroup().getNumber().equals(studentGroup.getNumber())) {
                    schedules.get(i).setStudentGroup(studentGroup);
                }
            }
        }
        dayScheduleDao.saveAll(schedules);
    }
}