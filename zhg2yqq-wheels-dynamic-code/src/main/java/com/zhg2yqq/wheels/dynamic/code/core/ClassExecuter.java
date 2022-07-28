/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

import com.zhg2yqq.wheels.dynamic.code.IClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.dto.ClassBean;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteCondition;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteParameter;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.exception.ExecuteException;
import com.zhg2yqq.wheels.dynamic.code.util.ClassUtils;

/**
 * 源码编译
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class ClassExecuter implements IClassExecuter<ExecuteResult> {
    /**
     * 调用类执行自定义方法
     * 
     * @param <E> 执行条件
     * @param parameter 调用Java方法必要参数
     * @param excuteCondition 执行条件
     * @return 方法执行结果
     * @throws ExecuteException .
     */
    @Override
    public <E extends ExecuteCondition> ExecuteResult runMethod(ExecuteParameter<? extends ClassBean> parameter,
                                                                E excuteCondition)
        throws ExecuteException {
        long runTakeTime = -1;
        Object returnVal = null;

        if (excuteCondition.isCalExecuteTime()) {
            long startTime = System.currentTimeMillis();
            // 调用类执行对应方法
            returnVal = this.run(parameter, excuteCondition.isUseSingleton());
            // 设置运行耗时
            runTakeTime = System.currentTimeMillis() - startTime;
        } else {
            // 调用类执行对应方法
            returnVal = this.run(parameter, excuteCondition.isUseSingleton());
        }
        return new ExecuteResult(runTakeTime, returnVal);
    }

    private Object run(ExecuteParameter<? extends ClassBean> parameter, boolean useSingleton)
        throws ExecuteException {
        try {
            ClassBean classBean = parameter.getClassBean();
            Object instance = classBean.getInstance();
            if (useSingleton) {
                // 单例
                if (instance == null) {
                    AtomicReference<Object> instanceReference = classBean.getInstanceReference();
                    instanceReference.compareAndSet(null,
                            ClassUtils.getClassInstance(classBean.getClazz()));
                    instance = instanceReference.get();
                }
            } else {
                // 非单例
                instance = ClassUtils.getClassInstance(classBean.getClazz());
            }
            return ClassUtils.runInstanceMethod(instance, parameter.getMethodName(),
                    parameter.getArgs());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new ExecuteException(e);
        } catch (InvocationTargetException e) {
            throw new ExecuteException(e, e.getCause());
        }
    }
}
