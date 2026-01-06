package com.example.lj_dm.controller;

import com.example.lj_dm.domain.ReadingLog;
import com.example.lj_dm.repository.ReadingLogRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reading")
public class ReadingLogController {

    private final ReadingLogRepository repository;

    @PostMapping("/today")
    public String checkToday(@RequestBody ReadingRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return "이름을 입력해 주세요.";
        }
        if (request.getCellName() == null || request.getCellName().isBlank()) {
            return "셀을 선택해 주세요.";
        }

        LocalDate today = LocalDate.now();

        if (repository.existsByReadingDateAndName(today, request.getName().trim())) {
            return "이미 오늘 기록이 있습니다.";
        }

        repository.save(new ReadingLog(
                request.getName().trim(),
                request.getCellName().trim(),
                today
        ));

        return "오늘 완독 체크 완료 🙏";
    }

    @Getter
    @Setter
    static class ReadingRequest {
        private String name;
        private String cellName;
    }
}
