package com.smartdine.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartdine.constant.MessageConstant;
import com.smartdine.context.BaseContext;
import com.smartdine.dto.CategoryDTO;
import com.smartdine.dto.CategoryPageQueryDTO;
import com.smartdine.entity.Category;
import com.smartdine.exception.DeletionNotAllowedException;
import com.smartdine.mapper.CategoryMapper;
import com.smartdine.mapper.DishMapper;
import com.smartdine.mapper.SetmealMapper;
import com.smartdine.result.PageResult;
import com.smartdine.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    //分类分页查询
    @Override
    public PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO) {

        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> page = categoryMapper.selectPage(categoryPageQueryDTO);

        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    //修改分类
    @Override
    public void update(Category category) {

//        Category category = new Category();
//        BeanUtils.copyProperties(categoryDTO,category);
//
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    //新增分类
    @Override
    public void insert(Category category) {

//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//
//        category.setCreateUser(BaseContext.getCurrentId());//从当前线程获取id
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    //启用禁用分类
    @Override
    public void change(Integer status, long id) {

        Category category = new Category();

        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        category.setId(id);
        category.setStatus(status);

        categoryMapper.change(category);
    }

    //根据id删除分类
    @Override
    public void delete(Integer id) {

        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.delete(id);

    }

    @Override
    public List<Category> selectCategory(Integer type) {

        return categoryMapper.selectCategory(type);
    }


}
