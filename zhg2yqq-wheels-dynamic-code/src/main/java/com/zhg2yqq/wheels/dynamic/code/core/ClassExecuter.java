/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.lang.reflect.InvocationTargetException;

import com.zhg2yqq.wheels.dynamic.code.IClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.exception.ExecuteException;
import com.zhg2yqq.wheels.dynamic.code.util.ClassUtils;

/**
 * 源码编译
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class ClassExecuter implements IClassExecuter {
    /**
     * 执行main方法
     * @throws ExecuteException 
     */
    public void runMainMethod(Class<?> clazz, CalTimeDTO calTime) throws ExecuteException {
        Parameters args = new Parameters();
        String[] pars = new String[0];
        args.add(pars);
        this.runMethod(clazz, "main", args, calTime);
    }

    /**
     * 调用类执行自定义方法
     * 
     * @param clazz 类
     * @param methodName 方法名
     * @param args 方法参数
     * @param calTime 计算执行时间参数
     * @return 方法执行结果
     * @throws ExecuteException
     */
    @Override
    public ExecuteResult runMethod(Class<?> clazz, String methodName, Parameters args,
                                   CalTimeDTO calTime)
        throws ExecuteException {
        long runTakeTime = -1;
        Object returnVal = null;

        if (calTime.isCalExecuteTime()) {
            long startTime = System.currentTimeMillis();
            // 调用类执行对应方法
            returnVal = this.run(clazz, methodName, args);
            // 设置运行耗时
            runTakeTime = System.currentTimeMillis() - startTime;
        } else {
            // 调用类执行对应方法
            returnVal = this.run(clazz, methodName, args);
        }
        return new ExecuteResult(runTakeTime, returnVal);
    }

    private Object run(Class<?> clazz, String methodName, Parameters args) throws ExecuteException {
        try {
            return ClassUtils.runClassMethod(clazz, methodName, args);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new ExecuteException(e);
        } catch (InvocationTargetException e) {
            throw new ExecuteException(e, e.getCause());
        }
    }
}
