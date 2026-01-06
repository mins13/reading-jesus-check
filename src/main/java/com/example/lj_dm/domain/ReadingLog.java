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
                // 같은 날짜 + 같은 이름은 1번만 저장되게
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

    // 누가
    @Column(nullable = false, length = 30)
    private String name;

    // 어느 셀
    @Column(name = "cell_name", nullable = false, length = 10)
    private String cellName;

    // 언제 읽었는지
    @Column(name = "reading_date", nullable = false)
    private LocalDate readingDate;

    // 생성 시간
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private Integer pages;


    public ReadingLog(String name, String cellName, LocalDate readingDate, Integer pages) {
        this.name = name;
        this.cellName = cellName;
        this.readingDate = readingDate;
        this.pages = pages;
        this.createdAt = LocalDateTime.now();

    }
}
