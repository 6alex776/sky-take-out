package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.*;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private ShoppingCartService shoppingCartService;

    // 生成订单号
    public String generateOrderNumber() {
        // 1. 生成当前时间字符串（年月日时分秒毫秒）
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timeStr = now.format(formatter);

        // 2. 生成3位随机数（0000-9999）
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String randomStr = String.format("%04d", randomNum);

        // 3. 组合生成订单号（例如：20230801153045123456）
        return "ORD" + timeStr + randomStr;
    }

    //提交订单
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        Long userId = BaseContext.getCurrentId();

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);

        orders.setNumber(generateOrderNumber());//获取订单号
        orders.setOrderTime(LocalDateTime.now());//获取下单时间
        orders.setUserId(userId);//获取当前用户id
        orders.setStatus(1);
        orders.setPayStatus(0);

        //从address_book查询phone等数据
        AddressBook addressBook = addressBookMapper.getById(userId);
        //插入orders
        BeanUtils.copyProperties(addressBook, orders);
        //orders.setAddressBookId(addressBook.getId());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());

        //在orders表中添加订单
        orderMapper.addList(orders);


        //在orderDetail赋值数据
        List<ShoppingCart> shoppingCart = shoppingCartMapper.list(userId);

        //TODO数组对象不能直接传值
        //BeanUtils.copyProperties(shoppingCart, orderDetail);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCart) {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(cart, detail);
            detail.setOrderId(orders.getId());
            orderDetailList.add(detail);
        }

        //orderDetailList.setOrderId(orders.getId());

        //在order_detail表中添加数据
        orderMapper.insert(orderDetailList);

        String number = orders.getNumber();//获取订单号
        //通过订单号获取VO信息
        OrderSubmitVO orderSubmitVOS = orderMapper.selectList(number);
        return orderSubmitVOS;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        /*JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }*/

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        //为替代微信支付成功后的数据库订单状态更新，多定义一个方法进行修改
        Integer OrderPaidStatus = Orders.PAID; //支付状态，已支付
        Integer OrderStatus = Orders.TO_BE_CONFIRMED;  //订单状态，待接单

        //发现没有将支付时间 check_out属性赋值，所以在这里更新
        LocalDateTime check_out_time = LocalDateTime.now();

        //获取订单号码
        String orderNumber = ordersPaymentDTO.getOrderNumber();

        log.info("调用updateStatus，用于替换微信支付更新数据库状态的问题");
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderNumber);

        return vo;
    }


    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    //TODO 综合分页查询
    //订单搜索
    @Override
    public PageResult page(OrdersPageQueryDTO ordersPageQueryDTO) {

        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.selectPage(ordersPageQueryDTO);

        List<OrderVO> orderVOList = getOrderVOList(page);
        PageResult pageResult = new PageResult(page.getTotal(), orderVOList);
        return pageResult;
    }

    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        // 需要返回订单菜品信息，自定义OrderVO响应结果
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // 将共同字段复制到OrderVO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);

                // 将订单菜品信息封装到orderVO中，并添加到orderVOList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            return x.getName() + "*" + x.getNumber() + ";";
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }


    //各个状态订单数量统计
    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();

        orderStatisticsVO.setToBeConfirmed(orderMapper.selectToBeConfirmed());
        orderStatisticsVO.setConfirmed(orderMapper.selectConfirmed());
        orderStatisticsVO.setDeliveryInProgress(orderMapper.selectDeliveryInProgress());

        return orderStatisticsVO;
    }

    //接单
    @Override
    public void confirm(Orders orders) {
        orderMapper.updateConfirm(orders.getId());
    }

    //取消订单
    @Override
    public void cancel(Orders orders) {
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateCancel(orders.getId(), orders.getCancelReason(), orders.getCancelTime());
    }

    //派送订单
    @Override
    public void delivery(Long id) {
        orderMapper.updateDelivery(id);
    }

    //完成订单
    @Override
    public void complete(Long id) {
        Orders orders = new Orders();
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateComplete(id, orders.getDeliveryTime());
    }

    //拒单
    @Override
    public void rejection(Orders orders) {
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.updateRejection(orders.getId(), orders.getRejectionReason(), orders.getCancelTime());
    }

    //TODO list封装
    //查询订单详情
    @Override
    public OrderVO details(Long id) {
        // 根据id查询订单
        Orders orders = orderMapper.getById(id);

        // 查询该订单对应的菜品/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到OrderVO并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    //再来一单
    @Override
    public void repetition(Long id) {

        //Long userId = BaseContext.getCurrentId();
        List<OrderDetail> list = orderMapper.selectByOrderId(id);
        for (OrderDetail detail : list) {
            ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
            BeanUtils.copyProperties(detail, shoppingCartDTO);
            shoppingCartService.addShoppingCart(shoppingCartDTO);
        }

    }

    //历史订单查询
    @Override
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = orderMapper.selectPage(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    //用户取消订单
    @Override
    public void userCancel(Long id) {
        Orders orders = new Orders();
        orders.setCancelTime(LocalDateTime.now());
        orders.setId(id);
        orderMapper.updateCancel(orders.getId(), orders.getCancelReason(), orders.getCancelTime());
    }

}
