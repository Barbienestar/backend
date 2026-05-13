package com.itesm.domain.repository;

import com.itesm.domain.models.Report;
import java.util.List;

public interface ReportRepository {
    Report save(Report report);
    List<Report> findByUserId(Long userId);
}