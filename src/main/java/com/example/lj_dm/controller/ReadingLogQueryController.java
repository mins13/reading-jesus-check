package com.example.lj_dm.controller;

import com.example.lj_dm.domain.ReadingLog;
import com.example.lj_dm.dto.ReadingLogResponse;
import com.example.lj_dm.service.DateRange;
import com.example.lj_dm.service.PeriodType;
import com.example.lj_dm.service.ReadingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reading")
public class ReadingLogQueryController {

    private final ReadingLogService service;

    @GetMapping("/logs")
    public LogsResponse getLogs(
            @RequestParam PeriodType period,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String cellName
    ) {
        DateRange range = service.resolveRange(period, date);
        List<ReadingLog> logs = service.getLogs(period, date, cellName);

        List<ReadingLogResponse> items = logs.stream()
                .map(ReadingLogResponse::from)
                .toList();

        return new LogsResponse(period.name(), range.start(), range.end(), cellName, items);
    }

    public record LogsResponse(
            String period,
            LocalDate startDate,
            LocalDate endDate,
            String cellName,
            List<ReadingLogResponse> items
    ) {}
}
