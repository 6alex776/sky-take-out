package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    @Select("select setmeal_id from sky_take_out.setmeal_dish where dish_id = #{id}")
    List<Long> getSetmealIdsByDishId(Long id);

}
