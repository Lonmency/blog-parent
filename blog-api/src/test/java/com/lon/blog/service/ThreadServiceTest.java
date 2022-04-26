package com.lon.blog.service;

import com.lon.blog.dao.mapper.ArticleMapper;
import com.lon.blog.dao.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ThreadServiceTest {

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void testupdateArticleViewCountByRedis(){
        Article article = articleMapper.selectById(1405909844724051969L);
        threadService.updateArticleViewCountByRedis(article);
    }

}