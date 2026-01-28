package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 2026/1/25
     * 批量插入
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    @Delete("delete from dish_flavor where id = #{dishId}")
    void deleteById(Long dishId);

    void deleteByIds(List<Long> dishIds);

    /**
     * 2026/1/28
     * 根据ID查找菜品口味
     * @param id
     */
    @Select("select * from dish_flavor where id = #{dishId}")
    List<DishFlavor> getByDishId(Long id);
}
