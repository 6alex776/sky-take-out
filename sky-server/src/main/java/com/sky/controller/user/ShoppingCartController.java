package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "购物车接口")
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    //增加购物车
    @PostMapping("/add")
    @ApiOperation("增加购物车")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车");
        shoppingCartService.addShoppingCart(shoppingCartDTO);

        return Result.success();
    }


    //展示购物车
    @GetMapping("/list")
    @ApiOperation("展示购物车")
    public Result<List<ShoppingCart>> showShoppingCart() {

        log.info("展示购物车");

        Long currentEmpId = BaseContext.getCurrentId();

        List<ShoppingCart> list = shoppingCartService.showShoppingCart(currentEmpId);

        return Result.success(list);
    }

    //清空购物车
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }

    //删除购物车菜品
    @PostMapping("/sub")
    @ApiOperation("删除购物车菜品")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
         log.info("删除购物车菜品");
         shoppingCartService.sub(shoppingCartDTO);
         return Result.success();
    }
}
