/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import com.zhg2yqq.wheels.dynamic.code.dto.ClassBean;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteCondition;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteParameter;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.exception.ExecuteException;

/**
 * 类方法执行器
 * 
 * @param <R> 方法执行结果
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public interface IClassExecuter<R extends ExecuteResult> {
    /**
     * 调用类执行对应方法
     * 
     * @param <E> 执行条件
     * @param parameter 调用Java方法必要参数
     * @param excuteCondition 执行条件
     * @return 方法执行结果
     * @throws ExecuteException .
     */
    <E extends ExecuteCondition> R runMethod(ExecuteParameter<? extends ClassBean> parameter,
                                            E excuteCondition)
        throws ExecuteException;
}
