package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    //分页查询
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("查询套餐{}", setmealPageQueryDTO);

        PageResult pageResult =setmealService.page(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    //新增菜品
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody Setmeal setmeal) {
        log.info("新增菜品{}", setmeal);

        setmealService.addDish(setmeal);

        return Result.success();
    }
}
