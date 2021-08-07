package com.schedule.dao;

import com.schedule.modal.Teacher;

import java.util.List;

public interface TeacherDao extends Dao<Teacher> {
    List<Teacher> getTeachersByFullname(String fullname);
}
