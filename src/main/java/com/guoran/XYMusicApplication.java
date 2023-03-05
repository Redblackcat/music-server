package com.guoran;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.guroan.dao")
public class XYMusicApplication {
    public static void main(String[] args) {
        SpringApplication.run(XYMusicApplication.class, args);
    }
}