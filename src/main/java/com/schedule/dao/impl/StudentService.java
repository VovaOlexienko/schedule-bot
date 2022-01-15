package com.schedule.dao.impl;

import com.schedule.dao.StudentDao;
import com.schedule.modal.Student;
import com.schedule.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService implements StudentDao {

    private final StudentRepository studentRepository;

    @Override
    public Optional<Student> get(Long id) {
        return Optional.ofNullable(studentRepository.getById(id));
    }

    @Override
    public Student save(Student obj) {
        return studentRepository.save(obj);
    }

    @Override
    public void delete(Student obj) {
        studentRepository.delete(obj);
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getByChatId(Long chatId) {
        return Optional.ofNullable(studentRepository.findByChatId(chatId));
    }
}

