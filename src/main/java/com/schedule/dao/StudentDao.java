package com.schedule.dao;

import com.schedule.modal.Student;

import java.util.Optional;

public interface StudentDao extends Dao<Student> {

    Optional<Student> getByChatId(Long chatId);
}
