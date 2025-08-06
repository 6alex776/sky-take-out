package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderSubmitVO;
import org.apache.ibatis.annotations.Mapper;
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
     *
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    @Update("update sky_take_out.orders set `status` = #{status}, pay_status=#{payStatus},checkout_time=#{checkoutTime} where id = #{id}")
    void fakeUpdate(Orders orders);

    @Select("select * from sky_take_out.orders where number = #{orderNumber} and user_id = #{userId}")
    Orders getByNumberAndUserId(String outTradeNo, Long userId);

    /**
     * 用于替换微信支付更新数据库状态的问题
     *
     * @param orderStatus
     * @param orderPaidStatus
     */
    @Update("update sky_take_out.orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} " +
            "where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);

    //订单搜索
    Page<Orders> selectPage(OrdersPageQueryDTO ordersPageQueryDTO);

    //各个状态订单数量统计
    @Select("select count(status) from sky_take_out.orders where status = 2")
    Integer selectToBeConfirmed();

    @Select("select count(status) from sky_take_out.orders where status = 3")
    Integer selectConfirmed();

    @Select("select count(status) from sky_take_out.orders where status = 4")
    Integer selectDeliveryInProgress();

    //接单
    @Update("update sky_take_out.orders set status = 3 where id = #{id}")
    void updateConfirm(Long id);

    //取消订单
    void updateCancel(Long id, String cancelReason, LocalDateTime cancelTime);

    //派送订单
    @Update("update sky_take_out.orders set status = 4 where id = #{id}")
    void updateDelivery(Long id);

    @Update("update sky_take_out.orders set status = 5,  delivery_time = #{deliveryTime} where id = #{id}")
    void updateComplete(Long id, LocalDateTime deliveryTime);

    @Update("update sky_take_out.orders set status = 6,cancel_time = #{cancelTime},rejection_reason = #{rejectionReason},cancel_reason = #{rejectionReason} where id =#{id}")
    void updateRejection(Long id, String rejectionReason, LocalDateTime cancelTime);

    //TODO SQL语句拼接
//    @Select("select o.*,GROUP_CONCAT(CONCAT(od.name, '×', od.number) SEPARATOR ',') as orderDishes from sky_take_out.orders o left join sky_take_out.order_detail od on o.id = od.order_id where o.id = #{id}")
//    Orders selectById(Long id);
    @Select("select * from sky_take_out.orders where id=#{id}")
    Orders getById(Long id);

    @Select("select * from sky_take_out.order_detail where order_id = #{id}")
    List<OrderDetail> selectByOrderId(Long id);


    Page<Orders> selectHistory(OrdersPageQueryDTO ordersPageQueryDTO);

//    @Update("update sky_take_out.orders set status = 6,cancel_reason = #{cancelReason},cancel_time = #{cancelTime} where status =#{status}")
//    void cancelAll(LocalDateTime cancelTime, Integer status, String cancelReason);

    //超时订单
    @Update("update sky_take_out.orders set status = 6,cancel_reason = #{cancelReason},cancel_time = #{cancelTime} where status =#{status} and TIMESTAMPDIFF(MINUTE, order_time, #{cancelTime}) > 15")//后减前
    void cancelOutTime(LocalDateTime cancelTime, Integer status, String cancelReason);

    @Select("select id from sky_take_out.orders where number = #{orderNumber}")
    Long selectOrderId(String orderNumber);

    //Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
}
