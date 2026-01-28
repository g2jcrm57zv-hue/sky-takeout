package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

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
}
