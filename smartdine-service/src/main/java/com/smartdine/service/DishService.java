package com.smartdine.service;


import com.smartdine.dto.DishPageQueryDTO;
import com.smartdine.entity.Category;
import com.smartdine.entity.Dish;
import com.smartdine.entity.DishFlavor;
import com.smartdine.result.PageResult;
import com.smartdine.vo.DishVO;

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

    /**
     * 条件查询菜品和口�?
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
