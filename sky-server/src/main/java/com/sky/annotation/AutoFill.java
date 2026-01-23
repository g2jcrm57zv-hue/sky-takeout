package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 2026/1/13
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 */
@Target(ElementType.METHOD) // 表示仅可以加在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效，这样反射才能读取得到
public @interface AutoFill {
    OperationType value();
}
