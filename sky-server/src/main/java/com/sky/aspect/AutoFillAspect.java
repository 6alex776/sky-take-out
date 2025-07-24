//TODO 公共字段自动填充
package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")// 切入点：拦截带@AutoFill注解的mapper方法
    public void autoFillPointcut(){}

    //前置通知
    @Before("autoFillPointcut()")// 通过方法名绑定切入点
    public void autoFill(JoinPoint joinPoint){

        log.info("开始公共字段填充");

        //获取被拦截的方法上的操作类型
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法注解对象
        OperationType operationType = autoFill.value();//获取操作类型

        //获取方法的实体
        Object[] args = joinPoint.getArgs();

        if(args == null || args.length == 0){
            return;
        }

        Object object = args[0];

        //准备需要赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long createUserId = BaseContext.getCurrentId();

        //根据不同操作类型，分别进行操作
        if (operationType == OperationType.INSERT) {
            try{
                // 获取实体类中"setCreateTime"方法，参数类型为LocalDateTime
            Method setCreateTime = object.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
            Method setCreateUser = object.getClass().getDeclaredMethod("setCreateUser", Long.class);
            Method setUpdateTime = object.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
            Method setUpdateUser = object.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                // 调用setCreateTime方法，为object的createTime字段设置值为now（当前时间）
            setCreateTime.invoke(object,now);
            setCreateUser.invoke(object,createUserId);
            setUpdateTime.invoke(object,now);
            setUpdateUser.invoke(object,createUserId);
            }catch (Exception e) {
            }

        }else if (operationType == OperationType.UPDATE) {
            try{
                Method setUpdateTime = object.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                setUpdateTime.invoke(object,now);
                setUpdateUser.invoke(object,createUserId);
            }catch (Exception e) {
            }
        }

    }

}
