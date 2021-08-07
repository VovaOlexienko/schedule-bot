package com.schedule.dao.impl;

import com.schedule.dao.TeacherDao;
import com.schedule.modal.Teacher;
import com.schedule.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherService implements TeacherDao {

    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public Optional<Teacher> get(Long id) {
        return Optional.of(teacherRepository.getById(id));
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
    public List<Teacher> getTeachersByFullname(String fullname) {
        return teacherRepository.findByFullnameContainingIgnoreCaseOrderByFullname(fullname);
    }
}
