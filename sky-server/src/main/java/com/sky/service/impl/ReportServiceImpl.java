package com.sky.service.impl;

import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.out;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private WorkspaceService workspaceService;

    //统计营业额数据
    @Override
    public TurnoverReportVO turnover(LocalDate begin, LocalDate end) {
        //单日
       /* if (begin.isEqual(end)) {

            LocalDate nextDay = begin.plusDays(1);
            //查询日期
            List<LocalDate> dateTimeListOne = reportMapper.selectOne(begin, nextDay);
            //查询营业额
            List<BigDecimal> amountListOne = reportMapper.selectAmountOne(begin, nextDay);

            return format(dateTimeListOne, amountListOne);

        } else {
            //非单日
            //查询日期
            List<LocalDate> dateTimeList = reportMapper.selectDateTime(begin, end);
            //查询营业额
            List<BigDecimal> amountList = reportMapper.selectAmount(begin, end);

            return format(dateTimeList, amountList);
        }*/

        // 1. 生成查询范围内的所有日期（补全空日期）
        List<LocalDate> allDates = generateAllDates(begin, end);

        // 2. 查询按日汇总的营业额（用Map接收）
        List<Map<String, Object>> dailyTurnoverMaps;
        if (begin.isEqual(end)) {
            LocalDate nextDay = begin.plusDays(1);
            dailyTurnoverMaps = reportMapper.selectDailyAmountOneMap(begin, nextDay);
        } else {
            dailyTurnoverMaps = reportMapper.selectDailyAmountMap(begin, end);
        }

        // 3. 将Map转换为“LocalDate→BigDecimal”的映射（方便匹配）
        Map<LocalDate, BigDecimal> turnoverMap = new HashMap<>();
        for (Map<String, Object> map : dailyTurnoverMaps) {

            java.sql.Date sqlDate = (java.sql.Date) map.get("date"); // 正确获取java.sql.Date
            LocalDate date = sqlDate.toLocalDate(); // 转为LocalDate

            // 提取金额（处理null，转为0）
            BigDecimal amount = map.get("totalAmount") == null
                    ? BigDecimal.ZERO
                    : new BigDecimal(map.get("totalAmount").toString());

            turnoverMap.put(date, amount);
        }

        // 4. 构建完整的日期列表和金额列表（无数据填0）
        List<LocalDate> fullDateList = new ArrayList<>();
        List<BigDecimal> fullAmountList = new ArrayList<>();
        for (LocalDate date : allDates) {
            fullDateList.add(date);
            // 无数据则填0
            fullAmountList.add(turnoverMap.getOrDefault(date, BigDecimal.ZERO));
        }

        // 5. 调用format方法格式化
        return format(fullDateList, fullAmountList);
    }

    //统计员工数据
    @Override
    public UserReportVO user(LocalDate begin, LocalDate end) {

        List<LocalDate> allDates = generateAllDates(begin, end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : allDates) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);


            Map<String, Object> map = new HashMap<>();

            //用户总量
            map.put("end", endTime);
            Integer totalUser = reportMapper.selectUser(map);

            //新增用户
            map.put("begin", beginTime);
            Integer newUser = reportMapper.selectUser(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);

        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(allDates, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();

    }

    //统计订单数据
    @Override
    public OrderReportVO order(LocalDate begin, LocalDate end) {

        List<LocalDate> allDates = generateAllDates(begin, end);

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : allDates) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);


            Map<String, Object> map = new HashMap<>();

            //用户总量
            map.put("end", endTime);
            map.put("begin", beginTime);
            //每日订单数
            Integer orderCount = reportMapper.selectOrderCount(map);
            //每日有效订单数
            Integer validOrderCount = reportMapper.selectValidOrderCount(map);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);

        }
        //订单总数
        //Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer count = reportMapper.selectOrderCount(null);
        //有效订单数
        Integer validCount = reportMapper.selectValidOrderCount(null);
        //订单完成率
        double rate = (double) validCount / count;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(allDates, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(count)
                .validOrderCount(validCount)
                .orderCompletionRate(rate)
                .build();
    }

    //销量排名
    @Override
    public SalesTop10ReportVO top(LocalDate begin, LocalDate end) {

        List<String> nameList = reportMapper.selectNames(begin, end);
        List<Integer> numberList = reportMapper.selectNumbers(begin, end);

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    //导出数据
    @Override
    public void export(HttpServletResponse response) {
        //获取营业数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        //写入excel
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("file/运营数据报表模板.xlsx");

        try {
            XSSFWorkbook excel = new XSSFWorkbook(resource);
            //获取标签页
            XSSFSheet sheet = excel.getSheetAt(0);
            sheet.getRow(1).getCell(1).setCellValue("时间" + begin + "至" + end);

            sheet.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheet.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheet.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());

            sheet.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheet.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());


            //明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate dateTime = begin.plusDays(i);
                BusinessDataVO data = workspaceService.getBusinessData(LocalDateTime.of(dateTime, LocalTime.MIN), LocalDateTime.of(dateTime, LocalTime.MAX));

                XSSFRow row = sheet.getRow(i + 7);
                row.getCell(1).setCellValue(dateTime.toString());
                row.getCell(2).setCellValue(data.getTurnover());
                row.getCell(3).setCellValue(data.getValidOrderCount());
                row.getCell(4).setCellValue(data.getOrderCompletionRate());
                row.getCell(5).setCellValue(data.getUnitPrice());
                row.getCell(6).setCellValue(data.getNewUsers());
            }

            //通过输出流下载到浏览器
            ServletOutputStream stream = response.getOutputStream();
            excel.write(stream);

            out.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 生成begin到end之间的所有日期（工具方法）
    private List<LocalDate> generateAllDates(LocalDate begin, LocalDate end) {
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate current = begin;
        while (!current.isAfter(end)) {
            allDates.add(current);
            current = current.plusDays(1);
        }
        return allDates;
    }

    // 格式转化并输出
    public TurnoverReportVO format(List<LocalDate> ld, List<BigDecimal> bd) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();


        String dateList = ld.stream()
                // 提取日期部分并格式化
                .map(dateTime -> dateTime.format(formatter))
                .collect(Collectors.joining(","));

        String turnoverList = bd.stream()
                // 提取营业额并格式化
                .map(decimal -> decimal.abs().toString())
                .collect(Collectors.joining(","));

        turnoverReportVO.setDateList(dateList);
        turnoverReportVO.setTurnoverList(turnoverList);

        return turnoverReportVO;
    }

}
