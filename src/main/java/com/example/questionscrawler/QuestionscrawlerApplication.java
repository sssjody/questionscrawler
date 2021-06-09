package com.example.questionscrawler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.questionscrawler.dao")
public class QuestionscrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionscrawlerApplication.class, args);
    }

}
