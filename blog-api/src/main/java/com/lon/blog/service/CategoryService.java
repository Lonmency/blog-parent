package com.lon.blog.service;

import com.lon.blog.vo.CategoryVo;
import com.lon.blog.vo.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findIdAndName();

    Result findAll();

    Result categoryDetailById(Long id);
}
