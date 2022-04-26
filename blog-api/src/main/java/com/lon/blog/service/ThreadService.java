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

    //期望此操作在线程池 执行 不会影响原有的主线程
    //异步更新，不影响原有线程
    //或者放到redis里，然后定时固化到数据库里
    @Async("taskExecutor")
    public void updateArticleViewCountByRedis(Article article) {

        int viewCounts = article.getViewCounts();
        //或者保存到redis里，之后固化到mysql数据库中
        String articleRedisKey = "articleViewCount" + "_" + article.getId();

        //检查redis是否存在对应项,如果不存在的话去数据库里查询对应记录的阅读次数值
        if(StringUtils.isBlank(redisTemplate.opsForValue().get(articleRedisKey))){
            log.info("新建redisKey: " + articleRedisKey);
            redisTemplate.opsForValue().set(articleRedisKey,Integer.toString(viewCounts));
        }

        int articleViewCount = Integer.parseInt(redisTemplate.opsForValue().get(articleRedisKey));
        redisTemplate.opsForValue().set(articleRedisKey,String.valueOf(++articleViewCount));
    }

    @Async("taskExecutor")
    public void updateComentCountByRedis(ArticleMapper articleMapper, String articleId) {

        Article article = articleMapper.selectById(articleId);

        int commentCounts = article.getCommentCounts();
        //或者保存到redis里，之后固化到mysql数据库中
        String commentRedisKey = "comentCount" + "_" + articleId;

        //检查redis是否存在对应项
        if(StringUtils.isBlank(redisTemplate.opsForValue().get(commentRedisKey))){
            log.info("新建redisKey: " + commentRedisKey);
            redisTemplate.opsForValue().set(commentRedisKey,Integer.toString(commentCounts));
        }

        int commentCount = Integer.parseInt(redisTemplate.opsForValue().get(commentRedisKey));
        redisTemplate.opsForValue().set(commentRedisKey,String.valueOf(++commentCount));

    }
}
