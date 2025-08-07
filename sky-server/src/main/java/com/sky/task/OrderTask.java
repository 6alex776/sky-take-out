package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@EnableScheduling
public class OrderTask {


    @Autowired
    private OrderMapper orderMapper;

    //超时订单
    @Scheduled(cron = "0 0/3 * * * *")
    public void outOfTime(){
        log.info("处理支付超时订单");
        Orders orders = new Orders();
        orders.setCancelTime(LocalDateTime.now());
        orders.setStatus(1);
        orders.setCancelReason("支付超时");
        orderMapper.cancelOutTime(orders.getCancelTime(),orders.getStatus(),orders.getCancelReason());
    }

    //处理派送中订单
    @Scheduled(cron = "0 0 3 * * ? ")
    public void outOfSend(){
        log.info("处理派送超时订单");
        Orders orders = new Orders();
        orders.setCancelTime(LocalDateTime.now());
        orders.setStatus(4);
        orders.setCancelReason("派送超时");
        orderMapper.cancelOutTime(orders.getCancelTime(),orders.getStatus(),orders.getCancelReason());
    }

}
