/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zhg2yqq.wheels.dynamic.code.core.HotSwapClassLoader;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.util.ClassUtils;

/**
 * 执行Java代码
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class RunClassHandler extends AbstractRunHandler {
    /*
     * 缓存类
     */
    private Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<>();
    /**
     * 统一类加载器
     */
    private IClassLoader loader = new HotSwapClassLoader();

    public RunClassHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime) {
        this(compiler, executer, calTime, null);
    }

    /**
     * 类执行器
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem）
     */
    public RunClassHandler(IStringCompiler compiler, IClassExecuter executer, CalTimeDTO calTime,
            Map<String, String> hackers) {
        super(compiler, executer, calTime, hackers);
    }

    /**
     * 必须提前预加载类
     * 
     * @param sourceStrs 源码
     */
    public void preloadClass(List<String> sourceStrs) {
        for (String sourceStr : sourceStrs) {
            String fullClassName = ClassUtils.getFullClassName(sourceStr);
            Class<?> clazz = super.loadClass(fullClassName, sourceStr, loader);
            loadedClasses.put(fullClassName, clazz);
        }
    }

    /**
     * 调用类方法
     * 
     * @param fullClassName 类全名，例如java.util.Date
     * @param methodName 方法名，例如getTime
     * @param parameters 方法参数
     * @return 方法执行结果
     * @throws Exception
     */
    @Override
    public ExecuteResult run(String fullClassName, String methodName, Parameters parameters)
        throws Exception {
        Class<?> clazz = loadedClasses.get(fullClassName);
        return getExecuter().runMethod(clazz, methodName, parameters, getCalTime());
    }

}