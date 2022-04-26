package com.lon.blog.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lon.blog.dao.mapper.ArticleMapper;
import com.lon.blog.dao.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LoadRedisDataToMysqlSchedule {
    //TODO 配在数据库里
    private static int perPageSize = 3;

    @Autowired
    private ArticleMapper articleMapper;

    @Scheduled(cron = "#{@getCron}")
    public void loadDataToDb(){
        System.out.println("test schedule every 5 seconds");
    }

    //导入数据的话，可以直接导入所有redis内存中的数据，然后一个一个更新，但这样会存在redis内存数据太多，一次导入全部数据可能撑爆jvm内存
    //所以这里选择，先从mysql数据中分页获取所有文章的id，然后一个个去redis中找，进行更新后并删除掉redis中的项
    //最好的方式是实现redis分页查询，因为redis中的数据量还是小于等于mysql数据库的数据量re
    public void loadArticleCount(){
        int pageNum = calcultePageSize(articleMapper.selectCount(null));
        for (int i = 1; i <= pageNum; i++) {
//            Page<Article> page = new Page(i,perPageSize);
//            Page<Article> selectPage = articleMapper.selectPage(page, null);
//            List<Article> records = selectPage.getRecords();
//            System.out.println("records: " + records);

            Page<Long> page = new Page(i,perPageSize);
            Page<Long> selectPage = articleMapper.selectId(page);
            List<Long> records = selectPage.getRecords();
            System.out.println("records: " + records);
        }

    }

    public int calcultePageSize(int sumCount){
       return sumCount/perPageSize + 1;
    }



    @Bean
    public String getCron(){
        return "0/5 * * * * *";
    }
}