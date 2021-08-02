package com.schedule.repository;

import com.schedule.modal.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {
    StudentGroup findByNumber(String number);
}
