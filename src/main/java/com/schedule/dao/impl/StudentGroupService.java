package com.schedule.dao.impl;

import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.StudentGroup;
import com.schedule.repository.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentGroupService implements StudentGroupDao {

    @Autowired
    StudentGroupRepository studentGroupRepository;

    @Override
    public Optional<StudentGroup> get(Long id) {
        return Optional.of(studentGroupRepository.getById(id));
    }

    @Override
    public StudentGroup save(StudentGroup obj) {
        return studentGroupRepository.save(obj);
    }

    @Override
    public void delete(StudentGroup obj) {
        studentGroupRepository.delete(obj);
    }

    @Override
    public List<StudentGroup> getAll() {
        return studentGroupRepository.findAll();
    }

    @Override
    public Optional<StudentGroup> getStudentGroupByNumber(String number) {
        return Optional.ofNullable(studentGroupRepository.findByNumber(number));
    }
}
