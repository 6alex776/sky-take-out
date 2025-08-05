package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    @Select("select name,number from sky_take_out.order_detail where order_id =#{id}")
    List<OrderDetail> getByOrderId(Long id);
}
