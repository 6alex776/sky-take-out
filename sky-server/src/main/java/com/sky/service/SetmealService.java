package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;

import java.util.List;

public interface SetmealService {


    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    //新增菜品
    void addDish(Setmeal setmeal);


    //删除套餐
    void delete(List<Long> ids);

    void change(Integer status, long id);

    Setmeal selectById(Long id);

    void update(Setmeal setmeal);
}
