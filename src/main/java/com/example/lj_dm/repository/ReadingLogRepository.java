package com.example.lj_dm.repository;

import com.example.lj_dm.domain.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    boolean existsByReadingDateAndName(LocalDate readingDate, String name);

    List<ReadingLog> findAllByReadingDateBetweenOrderByReadingDateAsc(LocalDate start, LocalDate end);

    List<ReadingLog> findAllByReadingDateBetweenAndCellNameOrderByReadingDateAsc(
            LocalDate start, LocalDate end, String cellName
    );
}
