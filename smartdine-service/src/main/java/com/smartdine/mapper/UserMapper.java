package com.smartdine.mapper;

import com.smartdine.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    @Select("SELECT * from user where id = #{userId}")
    User getById(Long userId);
}
