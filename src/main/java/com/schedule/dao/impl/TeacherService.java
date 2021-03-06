package com.schedule.dao.impl;

import com.schedule.dao.TeacherDao;
import com.schedule.modal.Teacher;
import com.schedule.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService implements TeacherDao {

    private final TeacherRepository teacherRepository;

    @Override
    public Optional<Teacher> get(Long id) {
        return Optional.ofNullable(teacherRepository.getById(id));
    }

    @Override
    public Teacher save(Teacher obj) {
        return teacherRepository.save(obj);
    }

    @Override
    public void delete(Teacher obj) {
        teacherRepository.delete(obj);
    }

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> getAll(Pageable pageable) {
        return teacherRepository.getAll(pageable);
    }

    @Override
    public List<Teacher> getByFullname(String fullname, Pageable pageable) {
        return teacherRepository.findByFullnameContainingIgnoreCase(fullname, pageable);
    }
}
