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
public class RunSourceHandler extends AbstractRunHandler {
    /*
     * 缓存类
     */
    private final ConcurrentMap<String, Class<?>> loadedClasses;

    public RunSourceHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime) {
        this(compiler, executer, calTime, null);
    }

    /**
     * 编译器
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem）
     */
    public RunSourceHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime,
            Map<String, String> hackers) {
        this(compiler, executer, calTime, hackers, 100);
    }

    public RunSourceHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime,
            int cacheSize) {
        this(compiler, executer, calTime, null, cacheSize);
    }

    /**
     * 编译器
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem）
     * @param cacheSize 缓存Class容器大小，超出容器大小将会以最近最少使用原则删除原数据
     */
    public RunSourceHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime,
            Map<String, String> hackers, int cacheSize) {
        super(compiler, executer, calTime, hackers);
        loadedClasses = new ConcurrentLinkedHashMap.Builder<String, Class<?>>()
                .maximumWeightedCapacity(cacheSize).weigher(Weighers.singleton()).build();
    }

    /**
     * 通过源码获取类，并调用方法。
     * 若不存在加载的类，将会编译源码缓存加载后的类；存在则直接返回类
     * 
     * @param source 源码
     * @param methodName 方法名
     * @param parameters 方法参数
     * @return 方法执行结果
     * @throws BaseDynamicException
     */
    public ExecuteResult runSourceJava(String source, String methodName, Parameters parameters)
        throws BaseDynamicException {
        return this.runSourceJava(source, methodName, parameters, false);
    }

    /**
     * 通过源码获取类，并调用方法
     * 
     * @param source 源码
     * @param methodName 方法名
     * @param parameters 方法参数
     * @param reloadClass 是否重新编译加载类
     * @return 方法执行结果
     * @throws BaseDynamicException
     */
    public ExecuteResult runSourceJava(String source, String methodName, Parameters parameters,
                                       boolean reloadClass)
        throws BaseDynamicException {
        if (reloadClass) {
            Class<?> clazz = this.reloadClassFromSource(source);
            return getExecuter().runMethod(clazz, methodName, parameters, getCalTime());
        }
        Class<?> clazz = this.loadClassFromSource(source);
        return getExecuter().runMethod(clazz, methodName, parameters, getCalTime());
    }

    /**
     * 加载Class，如果重复加载Class类，将直接返回之前的Class。
     * 每个class都对应独立的ClassLoader
     * 
     * @param sourceStr
     * @return
     * @throws CompileException 
     * @throws ClassLoadException 
     */
    public Class<?> loadClassFromSource(String sourceStr)
        throws CompileException, ClassLoadException {
        String fullClassName = ClassUtils.getFullClassName(sourceStr);
        try {
            return loadedClasses.computeIfAbsent(fullClassName, className -> {
                try {
                    return loadClass(className, sourceStr);
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

    /**
     * 重新加载Class，如果存在原始Class将会覆盖，返回新Class。
     * 因同一个ClassLoader实例不能重复加载相同类，所以这里每个class都对应独立的ClassLoader
     * 
     * @param sourceStr
     * @return
     * @throws ClassLoadException
     * @throws CompileException
     */
    public Class<?> reloadClassFromSource(String sourceStr)
        throws CompileException, ClassLoadException {
        String fullClassName = ClassUtils.getFullClassName(sourceStr);
        loadedClasses.put(fullClassName, loadClass(fullClassName, sourceStr));
        return loadedClasses.get(fullClassName);
    }
}