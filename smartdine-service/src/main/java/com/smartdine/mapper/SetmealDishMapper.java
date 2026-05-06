package com.smartdine.mapper;

import com.smartdine.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    @Select("select setmeal_id from setmeal_dish where dish_id = #{id}")
    List<Long> getSetmealIdsByDishId(Long id);

    //新增套餐菜品关系
    void insertBatch(List<SetmealDish> dishes);


    @Delete("delete from setmeal_dish where setmeal_id = #{id}")//删除套餐菜品表关联信息
    void delete(Long id);


    @Select("select * from setmeal_dish where setmeal_id = #{id}")//设置菜品列表
    List<SetmealDish> selectById(Long id);
}
