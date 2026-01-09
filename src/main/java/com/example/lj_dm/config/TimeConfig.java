package com.example.lj_dm.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeConfig {

    @PostConstruct
    public void init() {
        // ✅ JVM 기본 타임존을 한국(Asia/Seoul)로 고정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
