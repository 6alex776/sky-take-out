package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {

    //分类分页查询
    Page<Category> selectPage(CategoryPageQueryDTO categoryPageQueryDTO);

    //修改分类
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    //新增分类

    @Insert("insert into sky_take_out.category(id, type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{id},#{type},#{name},#{sort},1,#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    //启用禁用分类

    @Update("update sky_take_out.category set status = #{status},update_time = #{updateTime} where id = #{id}")
    @AutoFill(value = OperationType.UPDATE)
    void change(Category category);

    //根据id删除分类
    @Delete("delete from sky_take_out.category where id = #{id}")
    void delete(Integer id);


    List<Category> selectCategory(Integer type);
}
