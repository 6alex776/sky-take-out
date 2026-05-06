package com.smartdine.mapper;

import com.github.pagehelper.Page;
import com.smartdine.annotation.AutoFill;
import com.smartdine.dto.DishPageQueryDTO;
import com.smartdine.entity.Dish;
import com.smartdine.enumeration.OperationType;
import com.smartdine.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    //查询分类下对应的菜品数量

    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Integer categoryId);

    //新增菜品
    @AutoFill(value = OperationType.INSERT)
    void addDish(Dish dish);

    //分页查询
    Page<DishVO> selectPage(DishPageQueryDTO dishPageQueryDTO);


    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id = #{id}")
    void delete(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    //根据分类id查询菜品
    List<Dish> list(Dish dish);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
