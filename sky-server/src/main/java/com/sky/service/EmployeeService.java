package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);


    /**
     * 2026/1/8
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 2026/1/9
     * 分页查询
     * @param employeePageQueryDTO
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 2026/1/12
     * 启用、禁用员工登录
     * @param status
     * @param id
     * @return
     */
    Result startOrStop(Integer status, Long id);

    /**
     * 2026/1/22
     * 根据ID查询员工信息
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 2026/1/22
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    void update(EmployeeDTO employeeDTO);
}
