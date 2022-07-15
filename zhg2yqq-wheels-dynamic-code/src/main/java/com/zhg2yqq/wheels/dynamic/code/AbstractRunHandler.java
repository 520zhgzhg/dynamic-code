/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.zhg2yqq.wheels.dynamic.code.core.HotSwapClassLoader;
import com.zhg2yqq.wheels.dynamic.code.dto.ByteJavaFileObject;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;
import com.zhg2yqq.wheels.dynamic.code.exception.ClassLoadException;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;
import com.zhg2yqq.wheels.dynamic.code.util.ClassModifier;

/**
 * 执行器公共方法
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月13日
 */
public abstract class AbstractRunHandler {
    /*
     * 编译器
     */
    private IStringCompiler compiler;
    /*
     * 执行器
     */
    private IClassExecuter executer;
    /*
     * 安全替换（key:待替换的类全名，value:替换成的类全名）
     */
    private Map<String, String> hackers;
    /**
     * 统计耗时条件
     */
    private CalTimeDTO calTime;

    /**
     * 处理程序
     * 
     * @param compiler 编译器
     * @param executer 执行器
     * @param calTime 统计耗时条件
     */
    public AbstractRunHandler(IStringCompiler compiler, IClassExecuter executer,
            CalTimeDTO calTime) {
        this(compiler, executer, calTime, null);
    }

    /**
     * 处理程序
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System(也可java.lang.System)，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem(也可com.zhg2yqq.wheels.dynamic.code.hack.HackSystem)）
     */
    public AbstractRunHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime,
            Map<String, String> hackers) {
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
        this.calTime = calTime;
    }

    /**
     * 加载Class
     * 
     * @param fullClassName 类全名
     * @param sourceStr 源码
     * @param classLoader .
     * @return class类
     * @throws ClassLoadException
     * @throws CompileException
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
     * @throws ClassLoadException
     * @throws CompileException
     */
    protected Class<?> loadClass(String fullClassName, String sourceStr)
        throws CompileException, ClassLoadException {
        return this.load(fullClassName, sourceStr, HotSwapClassLoader::new);
    }

    private Class<?> load(String fullClassName, String sourceStr,
                          Supplier<IClassLoader> loaderSupplier)
        throws CompileException, ClassLoadException {
        CompileResult compileResult = compiler.compile(fullClassName, sourceStr, calTime);
        ByteJavaFileObject fileObject = compileResult.getFileObject();
        byte[] modiBytes = fileObject.getCompiledBytes();
        // 传入需要修改的字节数组
        ClassModifier classModifier = new ClassModifier(modiBytes);

        // 替换
        if (hackers != null && !hackers.isEmpty()) {
            for (Entry<String, String> hacker : hackers.entrySet()) {
                modiBytes = classModifier.modifyUTF8Constant(hacker.getKey(), hacker.getValue());
            }
        }
        IClassLoader classLoader = loaderSupplier.get();
        return classLoader.loadByte(fullClassName, modiBytes);
    }

    public IStringCompiler getCompiler() {
        return compiler;
    }

    public IClassExecuter getExecuter() {
        return executer;
    }

    public Map<String, String> getHackers() {
        return hackers;
    }

    public CalTimeDTO getCalTime() {
        return calTime;
    }
}
