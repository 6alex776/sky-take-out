package com.smartdine.mapper;

import com.smartdine.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    @Select("select name,number from order_detail where order_id =#{id}")
    List<OrderDetail> getByOrderId(Long id);

    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId1(Long orderId);
}
