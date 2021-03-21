package com.shortlink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(basePackages = {"com.shortlink.mapper"})
@SpringBootApplication
public class ShortlinkWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkWebApplication.class, args);
    }
}
