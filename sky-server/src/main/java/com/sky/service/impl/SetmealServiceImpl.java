package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.*;
import com.sky.exception.DeletionNotAllowedException;
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


    //删除套餐
    @Override
    public void delete(List<Long> ids) {
        //判断当前套餐是否在售卖
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        for (Long id : ids) {
            setmealMapper.delete(id);

            setmealDishMapper.delete(id);//删除套餐菜品表关联信息
        }
    }

    @Override
    public void change(Integer status, long id) {
        Setmeal setmeal = new Setmeal();

        setmeal.setStatus(status);
        setmeal.setId(id);

        setmealMapper.update(setmeal);
    }

    @Override
    public Setmeal selectById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);

        List<SetmealDish> dishes = setmealDishMapper.selectById(id);//设置菜品列表

        setmeal.setSetmealDishes(dishes);

        return setmeal;
    }

    @Override
    public void update(Setmeal setmeal) {
        setmealMapper.update(setmeal);

        setmealDishMapper.delete(setmeal.getId());

        List<SetmealDish> dishes = setmeal.getSetmealDishes();
        if (dishes != null) {
            dishes.forEach(dish -> {
                dish.setSetmealId(setmeal.getId());
            });
            setmealDishMapper.insertBatch(dishes);
        }
    }
}
