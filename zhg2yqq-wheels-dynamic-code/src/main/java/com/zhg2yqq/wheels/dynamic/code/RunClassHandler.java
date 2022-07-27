/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ClassBean;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteCondition;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteParameter;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.exception.BaseDynamicException;
import com.zhg2yqq.wheels.dynamic.code.exception.ClassLoadException;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;

/**
 * 执行Java代码，如果系统只需编译一次代码则推荐使用该执行器
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class RunClassHandler extends AbstractRunHandler<ExecuteResult> {
    /**
     * 缓存类
     */
    private Map<String, ClassBean<?>> cacheClasses = new ConcurrentHashMap<>();

    public RunClassHandler(IStringCompiler compiler, IClassExecuter<ExecuteResult> executer, CalTimeDTO calTime) {
        this(compiler, executer, calTime, null);
    }

    /**
     * 类处理程序
     * 
     * @param compiler 编译器
     * @param runner 执行器
     * @param calTime 统计耗时条件
     * @param hackers
     *            安全替换（key:待替换的类名,例如:java/lang/System，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackSystem）
     */
    public RunClassHandler(IStringCompiler compiler, IClassExecuter<ExecuteResult> executer, CalTimeDTO calTime,
            Map<String, String> hackers) {
        super(compiler, executer, calTime, hackers);
    }

    /**
     * 必须提前预加载类
     * 
     * @param sourceStrs 源码
     * @throws ClassLoadException .
     * @throws CompileException .
     */
    public void loadClassFromSources(List<String> sourceStrs) throws CompileException, ClassLoadException {
        for (String sourceStr : sourceStrs) {
            this.loadClassFromSource(sourceStr);
        }
    }

    /**
     * 执行Java方法
     * 
     * @param fullClassName 类全名，例如java.util.Date
     * @param methodName 方法名，例如getTime
     * @param parameters 方法参数
     * @param singleton 是否单例执行
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    @Override
    public ExecuteResult runMethod(String fullClassName, String methodName, Parameters parameters,
                                   boolean singleton)
        throws BaseDynamicException {
        ClassBean<?> classBean = this.getClassCache().get(fullClassName);
        if (classBean == null) {
            throw new BaseDynamicException(fullClassName + " 尚未编译源码");
        }
        ExecuteParameter<ClassBean<?>> parameter = new ExecuteParameter<>(classBean, methodName, parameters);
        ExecuteCondition condition = new ExecuteCondition(singleton, getCalTime().isCalExecuteTime());
        return getExecuter().runMethod(parameter, condition);
    }

    /**
     * 调用类方法
     * 若不存在加载的类，将会编译源码缓存加载后的类；存在则直接返回类
     * 将在1.8.2（不含）版本之后删除，由this.runMethod(fullClassName, methodName, parameters)替换
     * 
     * @param fullClassName 类全名，例如java.util.Date
     * @param methodName 方法名，例如getTime
     * @param parameters 方法参数
     * @return 方法执行结果
     * @throws BaseDynamicException .
     */
    @Deprecated
    public ExecuteResult runClassJava(String fullClassName, String methodName,
                                      Parameters parameters)
        throws BaseDynamicException {
        return this.runMethod(fullClassName, methodName, parameters);
    }

    @Override
    protected Map<String, ClassBean<?>> getClassCache() {
        return cacheClasses;
    }

}