/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.zhg2yqq.wheels.dynamic.code.config.BaseProperties;
import com.zhg2yqq.wheels.dynamic.code.core.HotSwapClassLoader;
import com.zhg2yqq.wheels.dynamic.code.dto.ClassBean;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.exception.BaseDynamicException;
import com.zhg2yqq.wheels.dynamic.code.exception.ClassLoadException;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;
import com.zhg2yqq.wheels.dynamic.code.util.ClassModifier;
import com.zhg2yqq.wheels.dynamic.code.util.ClassUtils;

/**
 * 执行器公共方法
 * 
 * @param <R> 执行结果类型
 * @param <T> 缓存对象
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月13日
 */
public abstract class AbstractRunHandler<R extends ExecuteResult, T extends ClassBean> {
    /**
     * 编译器
     */
    private IStringCompiler compiler;
    /**
     * 执行器
     */
    private IClassExecuter<R> executer;
    /**
     * 安全替换（key:待替换的类全名，value:替换成的类全名）
     */
    private Map<String, String> hackers;
    /**
     * 统计耗时条件
     */
    private BaseProperties properties;

    /**
     * 处理程序
     * 
     * @param compiler 编译器
     * @param executer 执行器
     * @param properties 配置
     */
    public AbstractRunHandler(IStringCompiler compiler, IClassExecuter<R> executer,
            BaseProperties properties) {
        this(compiler, executer, properties, null);
    }

    /**
     * 处理程序
     * 
     * @param compiler 编译器
     * @param executer 执行器
     * @param properties 配置
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System(也可java.lang.System)，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem(也可com.zhg2yqq.wheels.dynamic.code.hack.HackSystem)）
     */
    public AbstractRunHandler(IStringCompiler compiler, IClassExecuter<R> executer,
            BaseProperties properties, Map<String, String> hackers) {
        this.compiler = compiler;
        this.executer = executer;
        if (hackers != null) {
            Map<String, String> hks = new HashMap<>();
            for (Entry<String, String> hacker : hackers.entrySet()) {
                String key = hacker.getKey();
                String value = hacker.getValue();
                hks.put(key.replaceAll("\\.", "/"), value.replaceAll("\\.", "/"));
            }
            this.hackers = hks;
        }
        this.properties = properties;
    }

    /**
     * 执行Java方法（非单例模式执行）
     * 
     * @param sourceOrClass 源码或类名
     * @param methodName 方法名，例如getTime
     * @param parameters 方法参数
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    public R runMethod(String sourceOrClass, String methodName, Parameters parameters)
        throws BaseDynamicException {
        return this.runMethod(sourceOrClass, methodName, parameters, false);
    }

    /**
     * 执行Java方法
     * 
     * @param sourceOrClass 源码或类名
     * @param methodName 方法名，例如getTime
     * @param parameters 方法参数
     * @param singleton 是否单例执行
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    public abstract R runMethod(String sourceOrClass, String methodName, Parameters parameters,
                                boolean singleton)
        throws BaseDynamicException;

    /**
     * 加载Class，如果存在原始Class将会覆盖，返回新Class。
     * 
     * @param sourceStr 源码
     * @return 类
     * @throws CompileException .
     * @throws ClassLoadException .
     */
    public T loadClassFromSource(String sourceStr) throws CompileException, ClassLoadException {
        String fullClassName = ClassUtils.getFullClassName(sourceStr);
        Class<?> clazz = this.loadClass(fullClassName, sourceStr);
        this.getClassCache().put(fullClassName, this.buildClassBean(clazz));
        return this.getClassCache().get(fullClassName);
    }

    /**
     * 获取暂存加载的Class的缓存
     * 
     * @return .
     */
    protected abstract Map<String, T> getClassCache();

    /**
     * 构造缓存对象
     * 
     * @return 
     */
    protected abstract T buildClassBean(Class<?> clazz);

    /**
     * 加载Class
     * 
     * @param fullClassName 类全名
     * @param sourceStr 源码
     * @param classLoader .
     * @return class类
     * @throws ClassLoadException .
     * @throws CompileException .
     */
    protected Class<?> loadClass(String fullClassName, String sourceStr, IClassLoader classLoader)
        throws CompileException, ClassLoadException {
        return this.load(fullClassName, sourceStr, () -> classLoader);
    }

    /**
     * 加载Class
     * 
     * @param fullClassName 类全名
     * @param sourceStr 源码
     * @return class类
     * @throws ClassLoadException .
     * @throws CompileException .
     */
    protected Class<?> loadClass(String fullClassName, String sourceStr)
        throws CompileException, ClassLoadException {
        return this.load(fullClassName, sourceStr, HotSwapClassLoader::new);
    }
    
    protected IClassLoader getClassLoader() {
        if (properties.isSupportReload()) {
            return new HotSwapClassLoader();
        }
        return SingleClassLoaderHolder.classLoader;
    }

    private Class<?> load(String fullClassName, String sourceStr,
                          Supplier<IClassLoader> loaderSupplier)
        throws CompileException, ClassLoadException {
        // 编译
        CompileResult compileResult = compiler.compile(fullClassName, sourceStr, properties);
        // 获取编译后的字节码
        byte[] modiBytes = compileResult.getCompiledBytes();
        
        // 方法一：
        // 替换（主要是替换我们认为对系统运行有危害性的JRE类）
        if (hackers != null && !hackers.isEmpty()) {
            // 传入需要修改的字节数组
            ClassModifier classModifier = new ClassModifier(modiBytes);
            for (Entry<String, String> hacker : hackers.entrySet()) {
                modiBytes = classModifier.modifyUTF8Constant(hacker.getKey(), hacker.getValue());
            }
        }
        
        IClassLoader classLoader = loaderSupplier.get();
        return classLoader.loadByte(fullClassName, modiBytes);
    }
    
    /**
     * 类加载器
     */
    static class SingleClassLoaderHolder {
        static final IClassLoader classLoader = new HotSwapClassLoader();
    }

    public IStringCompiler getCompiler() {
        return compiler;
    }

    public IClassExecuter<R> getExecuter() {
        return executer;
    }

    public Map<String, String> getHackers() {
        return hackers;
    }

    public BaseProperties getProperties() {
        return properties;
    }
}
