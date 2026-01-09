package com.example.lj_dm.controller;

import com.example.lj_dm.domain.ReadingLog;
import com.example.lj_dm.repository.ReadingLogRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;

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

        // ✅ 무조건 한국 날짜 기준
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        String name = request.getName().trim();
        String cellName = request.getCellName().trim();

        if (repository.existsByReadingDateAndName(today, name)) {
            return "이미 오늘 기록이 있습니다.";
        }

        // ✅ 기록 방식:
        // - status=COMPLETED => "완독"
        // - status=PAGES + pagesNumber => "3" 같은 문자열
        // - 둘 다 없으면 기본 "완독" 처리(안전)
        String pagesText;
        if ("PAGES".equalsIgnoreCase(request.getStatus())) {
            Integer n = request.getPagesNumber();
            if (n == null || n < 0) return "장수는 0 이상의 숫자로 입력해 주세요.";
            pagesText = String.valueOf(n);
        } else {
            pagesText = "완독";
        }

        repository.save(new ReadingLog(name, cellName, today, pagesText));
        return "오늘 기록 완료 🙏";
    }

    @Getter
    @Setter
    static class ReadingRequest {
        private String name;
        private String cellName;

        /**
         * COMPLETED | PAGES
         */
        private String status;

        /**
         * status=PAGES 일 때만 사용
         */
        private Integer pagesNumber;
    }
}
