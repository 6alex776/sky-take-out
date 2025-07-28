package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.*;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<DishVO> page = setmealMapper.selectPage(setmealPageQueryDTO);

        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    //新增菜品
    @Override
    public void addDish(Setmeal setmeal) {
        setmealMapper.addMeal(setmeal); //新增菜品

        //获取insert语句的主键值
        Long id = setmeal.getId();

        List<SetmealDish> dishes = setmeal.getSetmealDishes();
        if (dishes != null) {
            dishes.forEach(dish -> {
//                dish.setId(setmeal.getId());
                dish.setSetmealId(setmeal.getId());
            });

            setmealDishMapper.insertBatch(dishes);//新增套餐菜品关系
        }
    }
}
