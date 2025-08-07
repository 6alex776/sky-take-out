package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@Slf4j
@Api(tags = "订单相关接口")
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //订单搜索
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜素")
    public Result<PageResult> search(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索");

        PageResult pageResult = orderService.page(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    //各个状态订单数量统计
    @GetMapping("/statistics")
    @ApiOperation("各个状态订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        log.info("各个状态订单数量统计");

        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);
    }

    //接单
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody Orders orders){
        log.info("接单");

        orderService.confirm(orders);
        return Result.success();
    }

    //取消订单
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody Orders orders){
        log.info("取消订单");
        orderService.cancel(orders);
        return Result.success();
    }

    //派送订单
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id){
        log.info("派送订单");
        orderService.delivery(id);
        return Result.success();
    }

    //完成订单
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id){
        log.info("派送订单");
        orderService.complete(id);
        return Result.success();
    }

    //拒单
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody Orders orders){
        log.info("拒单");
        orderService.rejection(orders);
        return Result.success();
    }

    //查询订单详情
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

}
