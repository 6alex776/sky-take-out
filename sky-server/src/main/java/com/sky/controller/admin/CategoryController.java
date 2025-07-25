package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关注解")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    //分类分页查询
    @GetMapping({"/page"})
    @ApiOperation("分类分页查询")
    public Result<PageResult> selectPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        if (categoryPageQueryDTO.getPage() <= 0) {
            categoryPageQueryDTO.setPage(1);
        }
        if (categoryPageQueryDTO.getPageSize() <= 0) {
            categoryPageQueryDTO.setPageSize(10);
        }

        log.info("查询页面{}", categoryPageQueryDTO);

        PageResult pageResult = categoryService.selectPage(categoryPageQueryDTO);

        return Result.success(pageResult);
    }

    @GetMapping({"/list"})
    @ApiOperation("分类查询")
    public Result<List<Category>> selectCategory (Integer type) {

        log.info("查询分类页面{}", type);

        List<Category> pageResult = categoryService.selectCategory(type);

        return Result.success(pageResult);
    }

    //修改分类
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody Category category){

        log.info("修改分类信息{}", category);

        categoryService.update(category);

        return Result.success();
    }

    //新增分类
    @PostMapping
    @ApiOperation("新增分类")
    public Result insert(@RequestBody Category category){
        log.info("新增分类{}", category);

        categoryService.insert(category);

        return Result.success();
    }

    //启用禁用分类
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result change(@PathVariable Integer status,long id){

        log.info("启用禁用分类{}", id);

        categoryService.change(status,id);

        return Result.success();
    }


    //根据id删除分类
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result delete(Integer id){

        log.info("删除分类{}", id);

        categoryService.delete(id);

        return Result.success();
    }


}
