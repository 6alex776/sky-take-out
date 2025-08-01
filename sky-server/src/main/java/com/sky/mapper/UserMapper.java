package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * from sky_take_out.user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    @Select("SELECT * from sky_take_out.user where id = #{userId}")
    User getById(Long userId);
}
