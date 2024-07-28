package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository which is used to access reporting data.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
