package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关修改")
public class DishController {

    @Autowired
    private DishService dishService;

    //新增菜品
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody Dish dish) {
        log.info("新增菜品{}", dish);

        dishService.addDish(dish);

        return Result.success();
    }

    //分页查询
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("查询菜品{}", dishPageQueryDTO);

        PageResult pageResult = dishService.page(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    //删除菜品
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids) {

        log.info("删除{}个菜品",ids.size());

        dishService.delete(ids);

        return Result.success();
    }

}
