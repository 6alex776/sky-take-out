package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    //查询分类下对应的菜品数量
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Integer categoryId);


    Page<DishVO> selectPage(SetmealPageQueryDTO setmealPageQueryDTO);


    //新增菜品
    @AutoFill(value = OperationType.INSERT)
    void addMeal(Setmeal setmeal);
}
