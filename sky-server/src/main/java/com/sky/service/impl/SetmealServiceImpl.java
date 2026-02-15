package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    DishMapper dishMapper;

    @Transactional
    public void saveWithDish(SetmealDTO setMealDTO) {
        // 将DTO属性赋值到实体类
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setMealDTO, setmeal);

        // 将实体类属性上传至数据库
        setMealMapper.insert(setmeal);

        // 上传完毕后，拿到数据库自动生成的主键id
        Long setmealId = setmeal.getId();
        
        // 通过id，将套餐与菜品作关联
        List<SetmealDish> setmealDishes = setMealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            for (SetmealDish dish : setmealDishes) {
                dish.setSetmealId(setmealId);
            }
        }

        setMealDishMapper.insertBatch(setmealDishes);
    }


    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 1.检查所有菜品，必须全为停售状态
        for (Long id : ids) {
            Setmeal setmeal = setMealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException("套餐售卖中，禁止删除！");
            }
        }

        // 2.批量删除菜品
        for (Long id : ids) {
            setMealMapper.deleteByid(id);
            setMealDishMapper.deleteById(id);
        }
    }


    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setMealMapper.update(setmeal);
        setMealDishMapper.deleteById(setmeal.getId());

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        // --- 修复致命 Bug 开始 ---
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                // 手动把套餐ID塞进去
                setmealDish.setSetmealId(setmealDTO.getId());
            });
            // 再保存
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    public void startOrStop(Integer status, Long id) {
        // 1. 如果是起售操作 (status == 1)
        if (status == StatusConstant.ENABLE) {
            // 查一下套餐里有没有包含停售的菜品
            List<Dish> dishList = dishMapper.getBySetmealId(id);

            if (dishList != null && dishList.size() > 0) {
                for (Dish dish : dishList) {
                    // 只要有一个菜品是停售的 (status == 0)
                    if (dish.getStatus() ==  StatusConstant.DISABLE) {
                        throw new SetmealEnableFailedException("套餐内包含未启售菜品，无法启售");
                    }
                }
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setMealMapper.update(setmeal);
    }

    /**
     * 根据id查询套餐和关联的菜品数据
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        // 1. 查套餐基本信息
        Setmeal setmeal = setMealMapper.getById(id);

        // 2. 查套餐关联的菜品
        List<SetmealDish> setmealDishes = setMealDishMapper.getBySetmealId(id);

        // 3. 封装 VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }
}
