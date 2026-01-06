package com.example.lj_dm.controller;

import com.example.lj_dm.domain.ReadingLog;
import com.example.lj_dm.service.PeriodType;
import com.example.lj_dm.service.ReadingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reading")
public class ReadingLogExportController {

    private final ReadingLogService service;

    /**
     * 예)
     * /api/reading/export?period=DAY
     * /api/reading/export?period=WEEK
     * /api/reading/export?period=MONTH
     *
     * (옵션)
     * /api/reading/export?period=WEEK&date=2026-01-06
     * /api/reading/export?period=WEEK&cellName=1셀
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam PeriodType period,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String cellName
    ) {
        List<ReadingLog> logs = service.getLogs(period, date, cellName);
        String csv = service.toCsv(logs);

        // 엑셀에서 한글 깨짐 방지용 UTF-8 BOM
        byte[] bom = new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF};
        byte[] body = csv.getBytes(StandardCharsets.UTF_8);

        byte[] bytes = new byte[bom.length + body.length];
        System.arraycopy(bom, 0, bytes, 0, bom.length);
        System.arraycopy(body, 0, bytes, bom.length, body.length);

        String filename = "reading-log-" + period.name().toLowerCase() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}
