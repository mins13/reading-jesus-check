package com.example.lj_dm.service;

import com.example.lj_dm.domain.ReadingLog;
import com.example.lj_dm.repository.ReadingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingLogService {

    private final ReadingLogRepository repository;

    public DateRange resolveRange(PeriodType period, LocalDate 기준일) {
        LocalDate base = (기준일 == null) ? LocalDate.now() : 기준일;

        return switch (period) {
            case DAY -> new DateRange(base, base);

            case WEEK -> {
                // 월~일 기준
                LocalDate start = base.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate end = base.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                yield new DateRange(start, end);
            }

            case MONTH -> {
                LocalDate start = base.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate end = base.with(TemporalAdjusters.lastDayOfMonth());
                yield new DateRange(start, end);
            }
        };
    }

    public List<ReadingLog> getLogs(PeriodType period, LocalDate date, String cellName) {
        DateRange range = resolveRange(period, date);

        if (cellName == null || cellName.isBlank()) {
            return repository.findAllByReadingDateBetweenOrderByReadingDateAsc(range.start(), range.end());
        }
        return repository.findAllByReadingDateBetweenAndCellNameOrderByReadingDateAsc(range.start(), range.end(), cellName);
    }

    public String toCsv(List<ReadingLog> logs) {
        // CSV 헤더
        StringBuilder sb = new StringBuilder();
        sb.append("날짜,셀,이름,장수\n");

        for (ReadingLog log : logs) {
            sb.append(log.getReadingDate()).append(",")
                    .append(log.getCellName()).append(",")
                    .append(log.getPages() == null ? "" : log.getPages())
                    .append(log.getName()).append("\n");
        }

        return sb.toString();
    }
}
