package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//公共字段自动填充
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

//表示加了@AutoFill的方法是AOP对应的接口
public @interface AutoFill {

    //标记操作类型:update,insert，区分不同类型的操作
    OperationType value();
}
