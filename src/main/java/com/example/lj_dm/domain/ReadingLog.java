package com.example.lj_dm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "reading_log",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_reading_date_name", columnNames = {"reading_date", "name"})
        },
        indexes = {
                @Index(name = "idx_reading_date", columnList = "reading_date"),
                @Index(name = "idx_cell_name", columnList = "cell_name")
        }
)
public class ReadingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "cell_name", nullable = false, length = 10)
    private String cellName;

    @Column(name = "reading_date", nullable = false)
    private LocalDate readingDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * ✅ "완독" 또는 "3" 같은 문자열 저장
     */
    @Column(name = "pages_text", length = 20)
    private String pagesText;

    public ReadingLog(String name, String cellName, LocalDate readingDate, String pagesText) {
        this.name = name;
        this.cellName = cellName;
        this.readingDate = readingDate;
        this.pagesText = pagesText;
        this.createdAt = LocalDateTime.now();
    }
}
