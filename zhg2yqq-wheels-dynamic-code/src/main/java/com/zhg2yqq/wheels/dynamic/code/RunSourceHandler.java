/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ClassBean;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteCondition;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteParameter;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.exception.BaseDynamicException;
import com.zhg2yqq.wheels.dynamic.code.exception.ClassLoadException;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;
import com.zhg2yqq.wheels.dynamic.code.util.ClassUtils;

/**
 * 执行Java代码，缓存编译后的Class（容器大小默认100，采用最近最少使用策略）
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class RunSourceHandler extends AbstractRunHandler<ExecuteResult> {
    private static final int DEFAULT_CACHE_SIZE = 100;
    /**
     * 缓存类
     */
    private final ConcurrentMap<String, ClassBean<?>> cacheClasses;

    /**
     * 源码处理程序
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     */
    public RunSourceHandler(IStringCompiler compiler, IClassExecuter<ExecuteResult> executer, CalTimeDTO calTime) {
        this(compiler, executer, calTime, null);
    }

    /**
     * 源码处理程序
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem）
     */
    public RunSourceHandler(IStringCompiler compiler, IClassExecuter<ExecuteResult> executer, CalTimeDTO calTime,
            Map<String, String> hackers) {
        this(compiler, executer, calTime, hackers, DEFAULT_CACHE_SIZE);
    }

    /**
     * 源码处理程序
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param cacheSize 缓存Class容器大小，超出容器大小将会以最近最少使用原则删除原数据
     */
    public RunSourceHandler(IStringCompiler compiler, IClassExecuter<ExecuteResult> executer, CalTimeDTO calTime,
            int cacheSize) {
        this(compiler, executer, calTime, null, cacheSize);
    }

    /**
     * 源码处理程序
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem）
     * @param cacheSize 缓存Class容器大小，超出容器大小将会以最近最少使用原则删除原数据
     */
    public RunSourceHandler(IStringCompiler compiler, IClassExecuter<ExecuteResult> executer, CalTimeDTO calTime,
            Map<String, String> hackers, int cacheSize) {
        super(compiler, executer, calTime, hackers);
        this.cacheClasses = new ConcurrentLinkedHashMap.Builder<String, ClassBean<?>>()
                .maximumWeightedCapacity(cacheSize).weigher(Weighers.singleton()).build();
    }

    /**
     * 执行Java方法
     * 
     * @param source 源码
     * @param methodName 方法名，例如getTime
     * @param parameters 方法参数
     * @param singleton 是否单例执行
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    @Override
    public ExecuteResult runMethod(String source, String methodName, Parameters parameters,
                                   boolean singleton) throws BaseDynamicException  {
        return this.runMethod(source, methodName, parameters, singleton, false);
    }

    /**
     * 执行Java方法
     * 
     * @param source 源码
     * @param methodName 方法名，例如getTime
     * @param parameters 方法参数
     * @param singleton 是否单例执行
     * @param reloadClass 是否重新编译加载类；当为true时，singleton单例失效（即重新编译后，都须重新创建实例）
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    public ExecuteResult runMethod(String source, String methodName, Parameters parameters,
                                   boolean singleton, boolean reloadClass) throws BaseDynamicException  {
        ClassBean<?> classBean = null;
        if (reloadClass) {
            classBean = this.loadClassFromSource(source);
        } else {
            classBean = this.loadOrginalClassFromSource(source);
        }
        ExecuteParameter<ClassBean<?>> parameter = new ExecuteParameter<>(classBean, methodName, parameters);
        ExecuteCondition condition = new ExecuteCondition(singleton, getCalTime().isCalExecuteTime());
        return getExecuter().runMethod(parameter, condition);
    }

    /**
     * 通过源码获取类，并调用方法。
     * 若不存在加载的类，将会编译源码缓存加载后的类；存在则直接返回类
     * 将在1.8.2（不含）版本之后删除，由this.runMethod(source, methodName, parameters)替换
     * 
     * @param source 源码
     * @param methodName 方法名
     * @param parameters 方法参数
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    @Deprecated
    public ExecuteResult runSourceJava(String source, String methodName, Parameters parameters)
        throws BaseDynamicException {
        return this.runSourceJava(source, methodName, parameters, false);
    }

    /**
     * 通过源码获取类，并调用方法
     * 将在1.8.2（不含）版本之后删除，由this.runMethod(source, methodName, parameters, false, reloadClass)替换
     * 
     * @param source 源码
     * @param methodName 方法名
     * @param parameters 方法参数
     * @param reloadClass 是否重新编译加载类
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    @Deprecated
    public ExecuteResult runSourceJava(String source, String methodName, Parameters parameters,
                                       boolean reloadClass)
        throws BaseDynamicException {
        return this.runMethod(source, methodName, parameters, false, reloadClass);
    }

    /**
     * 加载Class，如果重复加载Class类，将直接返回之前的Class。
     * 每个class都对应独立的ClassLoader
     * 
     * @param sourceStr 源码
     * @return 类
     * @throws CompileException .
     * @throws ClassLoadException .
     */
    public ClassBean<?> loadOrginalClassFromSource(String sourceStr)
        throws CompileException, ClassLoadException {
        String fullClassName = ClassUtils.getFullClassName(sourceStr);
        try {
            return this.getClassCache().computeIfAbsent(fullClassName, className -> {
                try {
                    Class<?> clazz = loadClass(className, sourceStr);
                    return new ClassBean<>(clazz);
                } catch (CompileException | ClassLoadException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof CompileException) {
                throw (CompileException) e.getCause();
            } else if (e.getCause() instanceof ClassLoadException) {
                throw (ClassLoadException) e.getCause();
            } else {
                throw e;
            }
        }
    }

    @Override
    protected Map<String, ClassBean<?>> getClassCache() {
        return cacheClasses;
    }
}