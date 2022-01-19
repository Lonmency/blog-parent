package com.lon.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BlogApp {
    public static void main(String[] args) {
        log.info("Hey we go");
        SpringApplication.run(BlogApp.class,args);
    }
}
