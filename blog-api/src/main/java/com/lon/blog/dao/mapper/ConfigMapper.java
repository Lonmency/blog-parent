package com.lon.blog.dao.mapper;
import org.apache.ibatis.annotations.Param;

import com.lon.blog.dao.pojo.ConfigPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lonmac
* @description 针对表【lt_config】的数据库操作Mapper
* @createDate 2022-03-25 11:27:16
* @Entity com.lon.blog.dao.pojo.Config
*/
public interface ConfigMapper extends BaseMapper<ConfigPojo> {

    ConfigPojo selectOneByName(@Param("name") String name);
}



