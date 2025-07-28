package com.sky.service;


import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.ArrayList;
import java.util.List;

public interface DishService {

    //新增菜品
    void addDish(Dish dish);

    //分页查询
    PageResult page(DishPageQueryDTO dishPageQueryDTO);


    void delete(List<Long> ids);

    Dish selectById(Long id);

    void update(Dish dish);

    void change(Integer status, long id);

    //根据分类id查询菜品
    List<Dish> list(Long categoryId);
}
