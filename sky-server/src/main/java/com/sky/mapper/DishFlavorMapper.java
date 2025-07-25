package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatch(@Param("flavors") List<DishFlavor> flavors);


    @Delete("delete from sky_take_out.dish_flavor where dish_id = #{id}")
    void delete(Long id);
}
