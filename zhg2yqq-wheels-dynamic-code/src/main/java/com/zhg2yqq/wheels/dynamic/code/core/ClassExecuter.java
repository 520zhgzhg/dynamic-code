/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
     * 线程池
     */
    private final ExecutorService executor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            2 * Runtime.getRuntime().availableProcessors(), 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<Runnable>());

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
            // 耗时统计
            long startTime = System.currentTimeMillis();
            // 调用类执行对应方法
            returnVal = this.run(parameter, excuteCondition);
            // 设置运行耗时
            runTakeTime = System.currentTimeMillis() - startTime;
        } else {
            // 调用类执行对应方法
            returnVal = this.run(parameter, excuteCondition);
        }
        return new ExecuteResult(runTakeTime, returnVal);
    }

    private Object run(ExecuteParameter<? extends ClassBean> parameter, ExecuteCondition excuteCondition)
        throws ExecuteException {
        try {
            ClassBean classBean = parameter.getClassBean();
            final Object instance;
            if (excuteCondition.isUseSingleton()) {
                // 单例
                if (classBean.getInstance() == null) {
                    AtomicReference<Object> instanceReference = classBean.getInstanceReference();
                    instanceReference.compareAndSet(null,
                            ClassUtils.getClassInstance(classBean.getClazz()));
                }
                instance = classBean.getInstance();
            } else {
                // 非单例
                instance = ClassUtils.getClassInstance(classBean.getClazz());
            }

            if (excuteCondition.getExecuteTimeOut() > 0) {
                // 设置方法超时
                Future<Object> future = executor.submit(() -> 
                    ClassUtils.runInstanceMethod(instance, parameter.getMethodName(),
                            parameter.getArgs())
                );
                return future.get(excuteCondition.getExecuteTimeOut(), TimeUnit.MILLISECONDS);
            } else {
                return ClassUtils.runInstanceMethod(instance, parameter.getMethodName(),
                        parameter.getArgs());
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InterruptedException | TimeoutException e) {
            throw new ExecuteException(e);
        } catch (InvocationTargetException e) {
            throw new ExecuteException(e, e.getCause());
        } catch (ExecutionException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                throw new ExecuteException(e, e.getCause().getCause());
            }
            throw new ExecuteException(e);
        }
    }
}
