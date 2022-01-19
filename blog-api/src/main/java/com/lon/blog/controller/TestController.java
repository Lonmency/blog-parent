package com.lon.blog.controller;

import com.lon.blog.dao.pojo.SysUser;
import com.lon.blog.utils.UserThreadLocal;
import com.lon.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
