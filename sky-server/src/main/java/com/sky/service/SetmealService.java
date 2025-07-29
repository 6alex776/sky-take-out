package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

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

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

}
