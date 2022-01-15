package com.schedule.dao.impl;

import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.StudentGroup;
import com.schedule.repository.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentGroupService implements StudentGroupDao {

    private final StudentGroupRepository studentGroupRepository;

    @Override
    public Optional<StudentGroup> get(Long id) {
        return Optional.ofNullable(studentGroupRepository.getById(id));
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
    public Optional<StudentGroup> getByNumber(String number) {
        return Optional.ofNullable(studentGroupRepository.findByNumber(number));
    }
}
