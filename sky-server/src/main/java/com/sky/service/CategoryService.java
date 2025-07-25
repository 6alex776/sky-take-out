package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    //分类分页查询
    PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO);

    //修改分类
    void update(Category category);

    //新增分类
    void insert(Category category);

    //启用禁用分类
    void change(Integer status, long id);

    //根据id删除分类
    void delete(Integer id);


    List<Category> selectCategory(Integer type);
}
