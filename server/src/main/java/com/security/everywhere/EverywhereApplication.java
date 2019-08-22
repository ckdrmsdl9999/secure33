package com.security.everywhere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@EnableScheduling  // 작업 스케쥴러 활성화
@SpringBootApplication
public class EverywhereApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverywhereApplication.class, args);
    }

    // 단일 쓰레드를 사용할 경우
    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}