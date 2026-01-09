package com.example.lj_dm.dto;

import com.example.lj_dm.domain.ReadingLog;

import java.time.LocalDate;

public record ReadingLogResponse(
        Long id,
        String name,
        String cellName,
        LocalDate readingDate,
        String pagesText
) {
    public static ReadingLogResponse from(ReadingLog log) {
        return new ReadingLogResponse(
                log.getId(),
                log.getName(),
                log.getCellName(),
                log.getReadingDate(),
                log.getPagesText()
        );
    }
}
