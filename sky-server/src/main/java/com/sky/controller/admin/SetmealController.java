package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //新增套餐
    @PostMapping
    @ApiOperation("新增套餐")
    @Cacheable(cacheNames = "setmealCache",key = "setmeal.categoryId")//springCache精确清理redis缓存
    public Result addDish(@RequestBody Setmeal setmeal) {
        log.info("新增套餐{}", setmeal);

        setmealService.addDish(setmeal);

        return Result.success();
    }

    //删除套餐
    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)//springCache清理redis缓存
    public Result delete(@RequestParam List<Long> ids) {

        log.info("删除{}个菜品",ids.size());

        setmealService.delete(ids);

        return Result.success();
    }

    //启用禁用套餐
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)//springCache清理redis缓存
    public Result change(@PathVariable Integer status,long id){

        log.info("启用禁用分类{}", id);

        setmealService.change(status,id);

        return Result.success();
    }

    //查询回显
    @GetMapping(value = "/{id}")
    @ApiOperation("查询回显")
    public Result<Setmeal> selectById(@PathVariable Long id){
        log.info("查询套餐{}",id);

        Setmeal setmeal = setmealService.selectById(id);

        return Result.success(setmeal);
    }

    //修改套餐
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)//springCache清理redis缓存
    public Result update(@RequestBody Setmeal setmeal){
        log.info("修改套餐{}",setmeal);

        setmealService.update(setmeal);

        return Result.success();

    }
}
