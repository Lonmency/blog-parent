package com.lon.blog.service;

import com.lon.blog.dao.pojo.ConfigPojo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lonmac
* @description 针对表【lt_config】的数据库操作Service
* @createDate 2022-03-25 11:27:16
*/
public interface ConfigService extends IService<ConfigPojo> {

    String findValueByName(String name);

}
