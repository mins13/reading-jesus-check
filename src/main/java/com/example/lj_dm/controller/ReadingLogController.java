package com.example.lj_dm.controller;

import com.example.lj_dm.domain.ReadingLog;
import com.example.lj_dm.repository.ReadingLogRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

        String name = request.getName().trim();
        String cell = request.getCellName().trim();

        LocalDate today = LocalDate.now();

        if (repository.existsByReadingDateAndName(today, name)) {
            return "이미 오늘 기록이 있습니다.";
        }

        // ✅ status 처리 (없으면 기존 방식 호환)
        Integer pagesToSave = null;

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            // 기존 호환: pages가 있으면 장수, 없으면 완독
            pagesToSave = request.getPages();
        } else {
            String st = request.getStatus().trim().toUpperCase();

            if ("COMPLETED".equals(st) || "DONE".equals(st)) {
                pagesToSave = null; // ✅ 완독
            } else if ("PAGES".equals(st)) {
                if (request.getPages() == null || request.getPages() < 0) {
                    return "장수는 0 이상의 숫자로 입력해 주세요.";
                }
                pagesToSave = request.getPages();
            } else {
                // 알 수 없는 status 들어오면 안전하게 기존처럼 처리
                pagesToSave = request.getPages();
            }
        }

        repository.save(new ReadingLog(
                name,
                cell,
                today,
                pagesToSave
        ));

        return "오늘 기록 완료 🙏";
    }

    @Getter
    @Setter
    static class ReadingRequest {
        private String name;
        private String cellName;

        // ✅ 추가: COMPLETED 또는 PAGES
        private String status;

        // 장수 입력일 때 사용
        private Integer pages;
    }
}
