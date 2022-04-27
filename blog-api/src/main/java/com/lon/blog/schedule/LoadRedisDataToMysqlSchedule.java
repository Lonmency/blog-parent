package com.lon.blog.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lon.blog.dao.mapper.ArticleMapper;
import com.lon.blog.dao.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Component
@Slf4j
public class LoadRedisDataToMysqlSchedule {
    private final static String zsetKey = "artileIdZset";
    private final static String viewhKey = "viewCount";
    private final static String commenthKey = "commentCount";

    //TODO 配在数据库里
    private static int perPageSize = 3;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "#{@getCron}")
    public void loadDataToDb(){
        System.out.println("test schedule every 5 seconds");
        loadArticleAndViewCount();
    }

    //导入数据的话，可以直接导入所有redis内存中的数据，然后一个一个更新，但这样会存在redis内存数据太多，一次导入全部数据可能撑爆jvm内存
    //所以这里选择，先从mysql数据中分页获取所有文章的id，然后一个个去redis中找，进行更新后并删除掉redis中的项
    //最好的方式是实现redis分页查询，因为redis中的数据量还是小于等于mysql数据库的数据量re
    public void loadArticleAndViewCount(){
        Integer size = getSize(zsetKey);
        int pageNum = calcultePageSize(size);

        try{
            for (int i = 1; i <= pageNum; i++) {
                Set<Long> articleIdPage = getIDPage(zsetKey, i, perPageSize);
                log.info("articleIdPage: {}",articleIdPage);
                Iterator<Long> iterator = articleIdPage.iterator();
                while (iterator.hasNext()){
                    //根据id取出对应的hash项
                    Long id= iterator.next();
                    int viewCount = hget(id.toString(),viewhKey);
                    int commentCout = hget(id.toString(),commenthKey);
                    log.info("key:{} hkey:{} value:{}",id,viewhKey,viewCount);
                    log.info("key:{} hkey:{} value:{}",id,commenthKey,commentCout);

                    //更新浏览量和评论数
                    //如果数量等于0则不更新（hash项为空或者hget()出现异常）
                    //TODO 是否可以设置成批量更新
                    LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();

                    lambdaUpdateWrapper.set(Article::getViewCounts,viewCount)
                                       .set(Article::getCommentCounts,commentCout)
                                       .eq(Article::getId,id);

                    int update = articleMapper.update(null, lambdaUpdateWrapper);
                    log.info("更新的记录数:{}",update);
                }
            }
        } catch (Exception e){
            log.error("redis分页查询失败:{}",e.getMessage(),e);
        }

    }

    public int calcultePageSize(int sumCount){
       return sumCount/perPageSize + 1;
    }

    /**
     * 存放单个hash缓存
     * @param key 键
     * @param hkey 键
     * @return
     */
    public  int hget(String key, String hkey) {
        int num = 0;
        try {
            if(null == redisTemplate.opsForHash().get(key,hkey)){
                //目前如果不触发评论操作的话，无法将该文章的评论数加载到redis的hash项中，所以此时redis hash项的评论数为空，这里设置默认值为0
                return num;
            }
            int value = Integer.parseInt(redisTemplate.opsForHash().get(key,hkey).toString());
            return value;
        } catch (Exception e) {
            log.warn("hget {} = {}", key+hkey,  e);
        }
        return num;
    }




    /**
     * 分页取出 hash中hkey值
     * @param key zset key
     * @param offset score的起始位置,可理解为页码
     * @param count 取count个数据，可理解为每页的数据量
     * @return
     */
    public Set<Long> getIDPage(String key, int offset, int count){
//        Set<String> result = new HashSet<String>();
        Set<Long> resultInLong = new HashSet<>();
//        result = test;
//        result.add(new Long(1L));

        try {
            Long size = redisTemplate.opsForZSet().size(key);
            resultInLong = redisTemplate.opsForZSet().rangeByScore(key, 0, size, (offset - 1) * count, count);
            log.info(String.valueOf(resultInLong.getClass()));
            log.debug("getPage {}", key);
        } catch (Exception e) {
            log.warn("getPage {}", key, e);
        }
        return resultInLong;
    }

    /**
     * 计算key值对应的数量
     * @param key
     * @return
     */
    public  Integer getSize(String key){
        Integer num = 0;
        try {
            Long size = redisTemplate.opsForZSet().size(key);

            log.debug("getSize {}", key);
            return size.intValue();
        } catch (Exception e) {
            log.warn("getSize {}", key, e);
        }
        return num;
    }

    @Bean
    public String getCron(){
        return "* 0/2 * * * *";
    }
}