package com.smartdine.service;

import com.smartdine.vo.OrderReportVO;
import com.smartdine.vo.SalesTop10ReportVO;
import com.smartdine.vo.TurnoverReportVO;
import com.smartdine.vo.UserReportVO;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnover(LocalDate begin, LocalDate end);

    UserReportVO user(LocalDate begin, LocalDate end);

    OrderReportVO order(LocalDate begin, LocalDate end);

    SalesTop10ReportVO top(LocalDate begin, LocalDate end);

    void export(HttpServletResponse response);
}
