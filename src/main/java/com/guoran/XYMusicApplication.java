package com.guoran;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.guroan.dao")
@EnableScheduling
public class XYMusicApplication {
    public static void main(String[] args) {
        SpringApplication.run(XYMusicApplication.class, args);
    }
}