/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.zhg2yqq.wheels.dynamic.code.core.HotSwapClassLoader;
import com.zhg2yqq.wheels.dynamic.code.dto.ByteJavaFileObject;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.util.ClassModifier;

import sun.tools.java.CompilerError;

/**
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
    public AbstractRunHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime) {
        this(compiler, executer, calTime, null);
    }

    /**
     * 处理程序
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem）
     */
    public AbstractRunHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime,
            Map<String, String> hackers) {
        this.compiler = compiler;
        this.executer = executer;
        this.hackers = hackers;
        this.calTime = calTime;
    }

    /**
     * 调用源码或类方法
     * 
     * @param arg 源码字符串或类全名，由具体实现类决定参数内容
     * @param methodName 方法名
     * @param parameters 方法参数
     * @return 方法执行结果
     * @throws Exception .
     */
    public abstract ExecuteResult run(String arg, String methodName, Parameters parameters) throws Exception;

    /**
     * 加载Class
     * 
     * @param fullClassName 类全名
     * @param sourceStr 源码
     * @param classLoader .
     * @return class类
     */
    protected Class<?> loadClass(String fullClassName, String sourceStr, IClassLoader classLoader) {
        return this.load(fullClassName, sourceStr, () -> classLoader);
    }

    /**
     * 加载Class
     * 
     * @param fullClassName 类全名
     * @param sourceStr 源码
     * @return class类
     */
    protected Class<?> loadClass(String fullClassName, String sourceStr) {
        return this.load(fullClassName, sourceStr, HotSwapClassLoader::new);
    }

    private Class<?> load(String fullClassName, String sourceStr,
                          Supplier<IClassLoader> loaderSupplier) {
        CompileResult compileResult = compiler.compiler(fullClassName, sourceStr, calTime);
        if (compileResult.isSuccess()) {
            ByteJavaFileObject fileObject = compileResult.getFileObject();
            byte[] modiBytes = fileObject.getCompiledBytes();
            // 传入需要修改的字节数组
            ClassModifier classModifier = new ClassModifier(modiBytes);

            // 替换
            if (hackers != null && !hackers.isEmpty()) {
                for (Entry<String, String> hacker : hackers.entrySet()) {
                    modiBytes = classModifier.modifyUTF8Constant(hacker.getKey(),
                            hacker.getValue());
                }
            }
            try {
                IClassLoader classLoader = loaderSupplier.get();
                return classLoader.loadByte(fullClassName, modiBytes);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(fullClassName + " not found", e);
            }
        } else {
            throw new CompilerError(compileResult.getCompilerMessage());
        }
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
