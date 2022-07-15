/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.exception.ExecuteException;

/**
 * 类方法执行器
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public interface IClassExecuter {
    /**
     * 调用类执行对应方法
     * 
     * @param clazz 类
     * @param methodName 方法名
     * @param args 方法参数
     * @param calTime 计算执行时间参数
     * @return 方法执行结果
     * @throws ExecuteException
     */
    ExecuteResult runMethod(Class<?> clazz, String methodName, Parameters args, CalTimeDTO calTime)
        throws ExecuteException;
}
