package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;

public interface SetmealService {


    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    //新增菜品
    void addDish(Setmeal setmeal);
}
