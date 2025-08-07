package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    // 非单日查询：返回日期和对应的营业额总和（Map形式）
    @Select("SELECT " +
            "DATE(delivery_time) AS date, " +
            "SUM(amount) AS totalAmount " +
            "FROM sky_take_out.orders " +
            "WHERE status = 5 " +
            "AND delivery_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY DATE(delivery_time) " +
            "ORDER BY date")
    List<Map<String, Object>> selectDailyAmountMap(LocalDate begin, LocalDate end);

    // 单日查询：返回日期和对应的营业额总和（Map形式）
    @Select("SELECT " +
            "DATE(delivery_time) AS date, " +
            "SUM(amount) AS totalAmount " +
            "FROM sky_take_out.orders " +
            "WHERE status = 5 " +
            "AND delivery_time BETWEEN #{begin} AND #{nextDay} " +
            "GROUP BY DATE(delivery_time) " +
            "ORDER BY date")
    List<Map<String, Object>> selectDailyAmountOneMap(LocalDate begin, LocalDate nextDay);

    //统计员工数据
    Integer selectUser(Map<String, Object> map);

    //每日订单数
    Integer selectOrderCount(Map<String, Object> map);

    //每日有效订单数
    Integer selectValidOrderCount(Map<String, Object> map);

    //    @Select("select od.name" +
//            "        from sky_take_out.order_detail od" +
//            "                 left join sky_take_out.orders o on o.id = od.order_id" +
//            "        where o.status = 5" +
//            "          and delivery_time between #{begin} and #{end} " +
//            "group by dish_id, od.name; ")
    List<String> selectNames(LocalDate begin, LocalDate end); // 查询菜品名称列表

    List<Integer> selectNumbers(LocalDate begin, LocalDate end); // 查询对应数量列表


    /*@Select("select DATE(delivery_time) as date from sky_take_out.orders where status = 5 and delivery_time between #{begin} and #{end} group by DATE(delivery_time) order by date")
    List<LocalDate> selectDateTime(LocalDate begin, LocalDate end);

    @Select("SELECT SUM(amount) AS totalAmount " +  // 金额字段
            "FROM sky_take_out.orders " +
            "WHERE status = 5 " +
            "AND delivery_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY DATE(delivery_time)")
    List<BigDecimal> selectAmount(LocalDate begin, LocalDate end);

    @Select("select delivery_time from sky_take_out.orders where status = 5 and delivery_time between #{begin} and #{nextDay} ")
    List<LocalDate> selectOne(LocalDate begin, LocalDate nextDay);

    @Select("select SUM(amount) from sky_take_out.orders where status = 5 and delivery_time between #{begin} and #{nextDay} ")
    List<BigDecimal> selectAmountOne(LocalDate begin, LocalDate nextDay);*/
}
