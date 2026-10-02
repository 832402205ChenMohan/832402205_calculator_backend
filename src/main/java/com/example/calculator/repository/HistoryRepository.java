package com.example.calculator.repository;

import com.example.calculator.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryRecord, Long> {
    List<HistoryRecord> findAllByOrderByIdDesc();
}
