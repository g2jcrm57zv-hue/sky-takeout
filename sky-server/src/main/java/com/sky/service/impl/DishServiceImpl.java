package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;
    /**
     * 2026/1/25
     * 新增菜品和对应口味
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO){
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);
        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 循环语法糖
        if(flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });

            dishFlavorMapper.insertBatch(flavors); // 此处为批量插入，无需循环
        }
    }

    /**
     * 2026/1/26
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO){
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 2026/1/26
     * 菜品批量删除
     * @param ids
     * @return
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 1.判断是否为起售中菜品：
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 2.判断是否被套餐关联菜品：
        List<Long> setMealsIds = setMealDishMapper.getSetMeadIdsBydDishIds(ids);
        if (setMealsIds != null && setMealsIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 3.删除菜品表中的菜品数据：
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            // 4.删除菜品关联的口味数据：
//            dishFlavorMapper.deleteById(id);
//        }
//
//        return null;

        // sql:delete from dish where id in (?, ?, ?)
        dishMapper.deleteByIds(ids);

        // sql:delete from dish_flavor where dishId in (?, ?, ?)
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getByIDWithFlavor(Long id) {
        // 根据id查询菜品
        Dish dish = dishMapper.getById(id);
        // 根据id查询菜品口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 将查询到的结果封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    public void updateWithFlavor(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 修改菜品信息
        dishMapper.update(dish);
        // 删除原有口味
        dishFlavorMapper.deleteByDishId(dish.getId());
        // 插入现有口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });

            dishFlavorMapper.insertBatch(flavors); // 此处为批量插入，无需循环
        }
    }
}

