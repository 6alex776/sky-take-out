package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    //统计营业额数据
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnover(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {
        log.info("统计营业额数据");
        TurnoverReportVO reportVO = reportService.turnover(begin,end);
        return Result.success(reportVO);
    }

    //统计用户数据
    @GetMapping("/userStatistics")
    public Result<UserReportVO> user(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("统计用户数据");
        UserReportVO user = reportService.user(begin,end);
        return Result.success(user);
    }

    //统计订单数据
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> order(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("统计订单数据");
        OrderReportVO order = reportService.order(begin,end);
        return Result.success(order);
    }

    //销量排名
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("销量排名");
        SalesTop10ReportVO top = reportService.top(begin,end);
        return Result.success(top);
    }

    //导出数据
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        log.info("导出数据");
        reportService.export(response);
    }
}
