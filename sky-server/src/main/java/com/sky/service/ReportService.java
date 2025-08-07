package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnover(LocalDate begin, LocalDate end);

    UserReportVO user(LocalDate begin, LocalDate end);

    OrderReportVO order(LocalDate begin, LocalDate end);

    SalesTop10ReportVO top(LocalDate begin, LocalDate end);
}
