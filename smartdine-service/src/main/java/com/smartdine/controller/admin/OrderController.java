package com.smartdine.controller.admin;

import com.smartdine.dto.OrdersCancelDTO;
import com.smartdine.dto.OrdersConfirmDTO;
import com.smartdine.dto.OrdersPageQueryDTO;
import com.smartdine.dto.OrdersRejectionDTO;
import com.smartdine.entity.Orders;
import com.smartdine.result.PageResult;
import com.smartdine.result.Result;
import com.smartdine.service.OrderService;
import com.smartdine.vo.OrderStatisticsVO;
import com.smartdine.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //订单搜索
    @GetMapping("/conditionSearch")
    @ApiOperation("订单条件搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){

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
        log.info("完成订单");
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

    //订单详情
    @GetMapping("/details/{id}")
    @ApiOperation("订单详情")
    public Result<OrderVO> details(@PathVariable Long id){
        log.info("订单详情");
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }
}
