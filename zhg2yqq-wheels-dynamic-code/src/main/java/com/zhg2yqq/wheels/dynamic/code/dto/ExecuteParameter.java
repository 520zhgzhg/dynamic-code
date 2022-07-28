/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

/**
 * IExcuter执行器执行方法参数
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月23日
 */
public class ExecuteParameter<T extends ClassBean> {
    /**
     * 类与实例
     */
    private T classBean;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法参数
     */
    private Parameters args;

    public ExecuteParameter() {
        super();
    }

    /**
     * @param classBean 类与实例
     * @param methodName 方法名
     * @param args 方法参数
     */
    public ExecuteParameter(T classBean, String methodName, Parameters args) {
        super();
        this.classBean = classBean;
        this.methodName = methodName;
        this.args = args;
    }

    public T getClassBean() {
        return classBean;
    }

    public void setClassBean(T classBean) {
        this.classBean = classBean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Parameters getArgs() {
        return args;
    }

    public void setArgs(Parameters args) {
        this.args = args;
    }
}
