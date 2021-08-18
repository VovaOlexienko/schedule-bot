package com.schedule.dao;

import com.schedule.modal.Teacher;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherDao extends Dao<Teacher> {

    List<Teacher> getByFullname(String fullname, Pageable pageable);

    List<Teacher> getAll(Pageable pageable);
}
