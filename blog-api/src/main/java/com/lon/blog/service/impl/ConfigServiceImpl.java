package com.lon.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lon.blog.dao.pojo.ConfigPojo;
import com.lon.blog.service.ConfigService;
import com.lon.blog.dao.mapper.ConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author lonmac
* @description 针对表【lt_config】的数据库操作Service实现
* @createDate 2022-03-25 11:27:16
*/
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, ConfigPojo>
    implements ConfigService {


    @Override
    public String findValueByName(String name) {
        LambdaQueryWrapper<ConfigPojo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ConfigPojo :: getName, name);
        String value = getOne(queryWrapper).getValue();
        return value;
    }
}




