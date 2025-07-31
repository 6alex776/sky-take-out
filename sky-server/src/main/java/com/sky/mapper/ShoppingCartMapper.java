package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    //查询数据库中是否有对应商品
    List<ShoppingCart> selectCart(ShoppingCart shoppingCart);
    
    //增加商品数量
    @Update("update sky_take_out.shopping_cart set number = #{number} where id = #{id} ")
    void addNumber(ShoppingCart cart);

    //插入数据库
    void addShoppingCart(ShoppingCart shoppingCart);

    //展示购物车
    List<ShoppingCart> list(Long currentEmpId);

    //清空购物车
    @Delete("delete from sky_take_out.shopping_cart where user_id =#{userId}")
    void clean(Long userId);

    //根据套餐或菜品id查询数量
    Integer selectNumber(ShoppingCart shoppingCart);

    //删除一个数量
    int subNumber(ShoppingCart shoppingCart);

    @Delete("delete from sky_take_out.shopping_cart where number =0 and user_id =#{userId}")
    void sub(ShoppingCart shoppingCart);
}
