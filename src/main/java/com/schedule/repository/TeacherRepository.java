package com.schedule.repository;

import com.schedule.modal.Teacher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>  {
    List<Teacher> findByFullnameContainingIgnoreCase(String fullname, Pageable pageable);
    @Query("select new Teacher (t.id, t.fullname, t.universityEmail, t.email, t.tgNickname, t.photoUrl, t.phone) from Teacher t")
    List<Teacher> getAll(Pageable pageable);
}
