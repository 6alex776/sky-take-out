package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    //增加购物车
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        //1.判断当前购物车中的商品是否存在
        Long currentEmpId = BaseContext.getCurrentId();//获取当前用户id

        ShoppingCart shoppingCart = new ShoppingCart();

        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);//TODO 参数传值到shoppingCart
        shoppingCart.setUserId(currentEmpId);

        List<ShoppingCart> list = shoppingCartMapper.selectCart(shoppingCart);//查询数据库中是否有对应商品


        if (list != null && !list.isEmpty()) {
            //2.若存在，只需增加数量
            log.info("购物车中已存在对应菜品或套餐");
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.addNumber(cart);

        } else {

            //3.若不存在，插入数据
            log.info("购物车中不存在对应菜品或套餐");

            //添加套餐
            if (shoppingCartDTO.getSetmealId() != null) {

                Long id = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(id);

                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setDishId(setmeal.getId());
                shoppingCart.setName(setmeal.getName());

            } else {
                //添加菜品
                Long id = shoppingCart.getDishId();
                Dish dish = dishMapper.getById(id);

                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setDishId(dish.getId());
                shoppingCart.setName(dish.getName());

            }

            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);

            //插入数据库
            shoppingCartMapper.addShoppingCart(shoppingCart);
        }
    }

    //展示购物车
    @Override
    public List<ShoppingCart> showShoppingCart(Long currentEmpId) {

        List<ShoppingCart> shoppingCart = shoppingCartMapper.list(currentEmpId);


        return shoppingCart;
    }

    //清空购物车
    @Override
    public void clean() {

        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.clean(userId);
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        //根据套餐或菜品id查询数量
        Integer number = shoppingCartMapper.selectNumber(shoppingCart);

        //删除一个数量
        shoppingCart.setNumber(number -1);

        shoppingCartMapper.subNumber(shoppingCart);

        //判断菜品数量是否为0
        if(shoppingCart.getNumber() == 0){
            shoppingCartMapper.sub(shoppingCart);
        }
    }
}
