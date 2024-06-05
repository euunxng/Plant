package com.example.project.repository;

import com.example.project.domain.Calender;
import com.example.project.domain.CalenderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalenderRepository extends JpaRepository<Calender, CalenderId> {
    List<Calender> findByGroupId(Long groupId);

    @Query("SELECT COUNT(c) > 0 FROM Calender c WHERE c.groupId = :groupId")
    boolean existsByGroupId(@Param("groupId") Long groupId);

}