package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 2026/1/26
     * 根据菜品ID查询
     * @param dishIds
     * @return
     */
    List<Long> getSetMeadIdsBydDishIds(List<Long> dishIds);

    /**
     * 批量插入菜品至数据库
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 删除套餐关联的菜品
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteById(@Param("setmealId") Long setmealId);

    /**
     * 根据套餐id查询套餐和菜品的关联关系
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 根据菜品ID查询对应的套餐ID
     * @param dishIds
     * @return
     */
    // select setmeal_id from setmeal_dish where dish_id in (1,2,3)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
