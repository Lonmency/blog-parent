package com.lon.blog.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoadRedisDataToMysqlScheduleTest {

    @Autowired
    private LoadRedisDataToMysqlSchedule loadRedisDataToMysqlSchedule;

    @Test
    void loadArticleCount() {
//        loadRedisDataToMysqlSchedule.loadArticleCount();
    }

    @Test
    void calcultePageSize() {
    }
}