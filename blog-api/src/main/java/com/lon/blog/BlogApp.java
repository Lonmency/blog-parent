package com.lon.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class BlogApp {
    public static void main(String[] args) {
        log.info("Hey we go new");
        SpringApplication.run(BlogApp.class,args);
    }
}
