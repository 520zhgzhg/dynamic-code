/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import com.zhg2yqq.wheels.dynamic.code.IClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
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
     */
    public void runMainMethod(Class<?> clazz, CalTimeDTO calTime)
        throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
        IllegalAccessException, InvocationTargetException, UnsupportedEncodingException {
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
     * @throws Exception
     */
    @Override
    public ExecuteResult runMethod(Class<?> clazz, String methodName, Parameters args, CalTimeDTO calTime)
        throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
        IllegalAccessException, InvocationTargetException, UnsupportedEncodingException {
        long runTakeTime = -1;
        Object returnVal;

        if (calTime.isCalExecuteTime()) {
            long startTime = System.currentTimeMillis();
            // 调用类执行对应方法
            returnVal = ClassUtils.runClassMethod(clazz, methodName, args);
            // 设置运行耗时
            runTakeTime = System.currentTimeMillis() - startTime;
        } else {
            // 调用类执行对应方法
            returnVal = ClassUtils.runClassMethod(clazz, methodName, args);
        }
        return new ExecuteResult(runTakeTime, returnVal);
    }
}
