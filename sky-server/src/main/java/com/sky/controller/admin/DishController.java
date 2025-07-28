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

    //查询回显
    @GetMapping(value = "/{id}")
    @ApiOperation("查询回显")
    public Result<Dish> selectById(@PathVariable Long id){
        log.info("查询菜品{}",id);

        Dish dish = dishService.selectById(id);

        return Result.success(dish);
    }

    //修改菜品
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody Dish dish){
        log.info("修改菜品{}",dish);

        dishService.update(dish);

        return Result.success();

    }

    //启用禁用分类
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result change(@PathVariable Integer status,long id){

        log.info("启用禁用分类{}", id);

        dishService.change(status,id);

        return Result.success();
    }

    //根据分类id查询菜品
    @GetMapping({"/list"})
    @ApiOperation("分类查询")
    public Result<List<Dish>> selectDish (Long categoryId) {

        log.info("查询分类页面{}", categoryId);

        List<Dish> list = dishService.list(categoryId);

        return Result.success(list);
    }
}
