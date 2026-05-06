package com.smartdine.service;

import com.smartdine.dto.OrdersPageQueryDTO;
import com.smartdine.dto.OrdersPaymentDTO;
import com.smartdine.dto.OrdersSubmitDTO;
import com.smartdine.entity.Orders;
import com.smartdine.result.PageResult;
import com.smartdine.vo.OrderPaymentVO;
import com.smartdine.vo.OrderStatisticsVO;
import com.smartdine.vo.OrderSubmitVO;
import com.smartdine.vo.OrderVO;

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
     * 支付成功，修改订单状�?
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    //订单搜索
    PageResult page(OrdersPageQueryDTO ordersPageQueryDTO);

    //各个状态订单数量统�?
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
