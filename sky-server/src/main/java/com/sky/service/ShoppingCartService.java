package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    //增加购物车
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    //展示购物车
    List<ShoppingCart> showShoppingCart(Long currentEmpId);

    //清空购物车
    void clean();

    void sub(ShoppingCartDTO shoppingCartDTO);
}
