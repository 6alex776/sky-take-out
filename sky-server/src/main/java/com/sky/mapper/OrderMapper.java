package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderSubmitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    //在orders表中添加订单
    void addList(Orders orders);

    //通过订单号获取VO信息
    OrderSubmitVO selectList(String number);

    //在order_detail表中添加数据
    void insert(List<OrderDetail> orderDetailList);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Update("update sky_take_out.orders set `status` = #{status}, pay_status=#{payStatus},checkout_time=#{checkoutTime} where id = #{id}")
    void fakeUpdate(Orders orders);

    @Select("select * from sky_take_out.orders where number = #{orderNumber} and user_id = #{userId}")
    Orders getByNumberAndUserId(String outTradeNo, Long userId);

    /**
     * 用于替换微信支付更新数据库状态的问题
     * @param orderStatus
     * @param orderPaidStatus
     */
    @Update("update sky_take_out.orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} " +
            "where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);

}
