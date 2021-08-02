package com.schedule.dao;

import com.schedule.modal.StudentGroup;

import java.util.Optional;

public interface StudentGroupDao extends Dao<StudentGroup> {

    Optional<StudentGroup> getStudentGroupByNumber(String number);
}
