package com.lon.blog.service;

import com.lon.blog.vo.CategoryVo;
import com.lon.blog.vo.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
