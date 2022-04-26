package com.lon.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lon.blog.dao.mapper.ArticleMapper;
import com.lon.blog.dao.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThreadService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ArticleMapper articleMapper;

    //期望此操作在线程池 执行 不会影响原有的主线程
    //异步更新，不影响原有线程
    //或者放到redis里，然后定时固化到数据库里
    @Async("taskExecutor")
    public void updateArticleViewCountByRedis(Article article) {
        //目前mysql存储的浏览数
        int viewCounts = article.getViewCounts();

        //或者保存到redis里，之后固化到mysql数据库中
        String articleRedisKey = article.getId().toString();
        String hkey = "viewCount";

        //检查redis是否存在对应项,如果不存在的话去数据库里查询对应记录的阅读次数值
        //如果对应项不存在新建项并赋值
        if(null == redisTemplate.opsForHash().get(articleRedisKey, hkey)){
            log.info("新建redisKey:{}，hkey:{} 的hash ",articleRedisKey,hkey);
            redisTemplate.opsForHash().put(articleRedisKey,hkey,Integer.toString(viewCounts));
        }

        //自增1
        redisTemplate.opsForHash().increment(articleRedisKey,hkey,1);
    }

    @Async("taskExecutor")
    public void updateComentCountByRedis(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        //现有的评论数
        int commentCounts = article.getCommentCounts();
        //或者保存到redis里，之后固化到mysql数据库中
        String articleRedisKey = articleId.toString();
        String hkey = "commentCount";

        //检查redis是否存在对应项,如果不存在的话去数据库里查询对应记录的阅读次数值
        //如果对应项不存在新建项并赋值
        if(null == redisTemplate.opsForHash().get(articleRedisKey, hkey)){
            log.info("新建redisKey:{}，hkey:{} 的hash ",articleRedisKey,hkey);
            redisTemplate.opsForHash().put(articleRedisKey,hkey,Integer.toString(commentCounts));
        }

        //自增1
        redisTemplate.opsForHash().increment(articleRedisKey,hkey,1);
    }
}