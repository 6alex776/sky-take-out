package com.sky.service;


import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
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
}
