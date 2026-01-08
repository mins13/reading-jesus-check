package com.example.lj_dm.dto;

import com.example.lj_dm.domain.ReadingLog;

import java.time.LocalDate;

public record ReadingLogResponse(
        Long id,
        String name,
        String cellName,
        LocalDate readingDate,
        String pages   // ✅ Integer -> String
) {
    public static ReadingLogResponse from(ReadingLog log) {
        String pagesText = (log.getPages() == null) ? "완독" : String.valueOf(log.getPages());

        return new ReadingLogResponse(
                log.getId(),
                log.getName(),
                log.getCellName(),
                log.getReadingDate(),
                pagesText
        );
    }
}
