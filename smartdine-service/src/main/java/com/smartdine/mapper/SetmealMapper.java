package com.smartdine.mapper;

import com.github.pagehelper.Page;
import com.smartdine.annotation.AutoFill;
import com.smartdine.dto.SetmealPageQueryDTO;
import com.smartdine.entity.Setmeal;
import com.smartdine.entity.ShoppingCart;
import com.smartdine.enumeration.OperationType;
import com.smartdine.vo.DishItemVO;
import com.smartdine.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    //查询分类下对应的菜品数量
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Integer categoryId);


    Page<DishVO> selectPage(SetmealPageQueryDTO setmealPageQueryDTO);


    //新增菜品
    @AutoFill(value = OperationType.INSERT)
    void addMeal(Setmeal setmeal);

    //判断当前套餐是否在售出
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    //删除套餐
    @Delete("delete from setmeal where id = #{id}")
    void delete(Long id);


    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     *
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
