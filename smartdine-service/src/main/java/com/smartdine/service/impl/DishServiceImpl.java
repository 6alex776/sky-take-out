package com.smartdine.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smartdine.constant.MessageConstant;
import com.smartdine.constant.StatusConstant;
import com.smartdine.dto.DishPageQueryDTO;
import com.smartdine.entity.Dish;
import com.smartdine.entity.DishFlavor;
import com.smartdine.exception.DeletionNotAllowedException;
import com.smartdine.mapper.DishFlavorMapper;
import com.smartdine.mapper.DishMapper;
import com.smartdine.mapper.SetmealDishMapper;
import com.smartdine.result.PageResult;
import com.smartdine.service.DishService;
import com.smartdine.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    //新增菜品
    @Override
    public void addDish(Dish dish) {
        dishMapper.addDish(dish);

        //获取insert语句的主键值?
        Long id = dish.getId();

        List<DishFlavor> flavors = dish.getFlavors();
        if (flavors != null) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    //分页查询
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.selectPage(dishPageQueryDTO);

        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public void delete(List<Long> ids) {
        //判断当前菜品是否在售�?
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否关联了套�?
        for (Long id : ids) {
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishId(id);
            if (!setmealIds.isEmpty()) {
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
            }
        }

        for (Long id : ids) {
            //删除菜品数据
            dishMapper.delete(id);

            //删除菜品关联的口味数�?
            dishFlavorMapper.delete(id);
        }
    }

    @Override
    public Dish selectById(Long id) {

        Dish dish = dishMapper.getById(id);

        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);// 设置口味列表

        dish.setFlavors(flavors);

        return dish;

    }

    @Override
    public void update(Dish dish) {
        dishMapper.update(dish);

        dishFlavorMapper.delete(dish.getId());

        List<DishFlavor> flavors = dish.getFlavors();

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void change(Integer status, long id) {

        Dish dish = new Dish();

        dish.setStatus(status);
        dish.setId(id);

        dishMapper.update(dish);

    }



    //根据分类id查询菜品
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口�?
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口�?
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
