package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    //提交订单
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    //订单搜索
    PageResult page(OrdersPageQueryDTO ordersPageQueryDTO);

    //各个状态订单数量统计
    OrderStatisticsVO statistics();

    //接单
    void confirm(Orders orders);

    //取消订单
    void cancel(Orders orders);

    void delivery(Long id);

    void complete(Long id);

    void rejection(Orders orders);

    OrderVO details(Long id);

    void repetition(Long id);

    //历史订单查询
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    void userCancel(Long id);

    void reminder(Long id);
}
