//package com.lon.blog;
//
//import com.lon.blog.dao.mapper.ConfigMapper;
//import com.lon.blog.dao.pojo.ConfigPojo;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@SpringBootTest
//public class ConfigTest {
//
//    @Autowired
//    private ConfigMapper configMapper;
//
//    @Test
//    public void testSelect(){
//        List<ConfigPojo> configs = configMapper.selectList(null);
//
//        configs.forEach(System.out::println);
//    }
//
//    @Test
//    public void testInsert(){
//        ConfigPojo config = new ConfigPojo();
//        config.setName("3");
//        config.setValue("3");
//        configMapper.insert(config);
//    }
//
//}
