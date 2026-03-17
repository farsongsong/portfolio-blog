package com.example.demo;

import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class PortfolioApplication {

    private final AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

    // 앱 시작 시 관리자 계정 자동 생성 (admin / admin1234)
    @Bean
    public ApplicationRunner init() {
        return args -> authService.createAdminIfNotExists();
    }
}
