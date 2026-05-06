package com.smartdine.service;

import com.smartdine.dto.CategoryDTO;
import com.smartdine.dto.CategoryPageQueryDTO;
import com.smartdine.entity.Category;
import com.smartdine.result.PageResult;

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
