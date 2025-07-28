package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    //查询分类下对应的菜品数量

    @Select("select count(id) from sky_take_out.dish where category_id = #{categoryId}")
    Integer countByCategoryId(Integer categoryId);

    //新增菜品
    @AutoFill(value = OperationType.INSERT)
    void addDish(Dish dish);

    //分页查询
    Page<DishVO> selectPage(DishPageQueryDTO dishPageQueryDTO);


    @Select("select * from sky_take_out.dish where id = #{id}")
    Dish getById(Long id);

    @Delete("delete from sky_take_out.dish where id = #{id}")
    void delete(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    //根据分类id查询菜品
    List<Dish> list(Dish dish);
}
