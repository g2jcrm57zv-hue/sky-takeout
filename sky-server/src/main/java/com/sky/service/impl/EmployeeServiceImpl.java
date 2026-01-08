package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 2026/1/8
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        // 1.将DTO对象转换为实体对象
        Employee employee = new Employee(); // 此时属性为空

        // 2.为employee设置属性，通过Spring自带的BeanUtils为两个实例复制属性
        BeanUtils.copyProperties(employeeDTO, employee); // 此方法使用前提为两个实例的属性名一致

        // 3.第二步仅设置了部分属性，实体类中有一些属性是DTO类中没有的，在这里需要手动设置
        // 设置账号状态，默认为正常状态
        employee.setStatus(StatusConstant.ENABLE); // 1为正常，0为锁定，此处使用常量类，方便代码后期维护。

        // 设置密码，新增员工则设置默认密码123456
        employee.setPassword(DigestUtils
                .md5DigestAsHex
                (PasswordConstant
                        .DEFAULT_PASSWORD
                        .getBytes())); // 由于传入数据库，需要MD5加密，因此使用工具类方法加密。

        // 设置当前记录的创建时间与修改时间，即系统时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置当前记录人id和修改人id
        // 由于暂时无法获取到，因此写死
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }
}
