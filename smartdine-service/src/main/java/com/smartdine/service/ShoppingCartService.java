package com.smartdine.service;

import com.smartdine.dto.ShoppingCartDTO;
import com.smartdine.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    //增加购物�?
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    //展示购物�?
    List<ShoppingCart> showShoppingCart(Long currentEmpId);

    //清空购物车?
    void clean();

    void sub(ShoppingCartDTO shoppingCartDTO);
}
