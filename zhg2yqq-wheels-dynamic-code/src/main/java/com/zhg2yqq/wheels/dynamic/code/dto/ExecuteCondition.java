/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

/**
 * 执行条件
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月14日
 */
public class ExecuteCondition {
    /**
     * 执行时使用单例
     */
    private boolean useSingleton;
    /**
     * 统计计算类方法执行时间
     */
    private boolean calExecuteTime;
    /**
     * 方法执行超时时间（防止死循环占用，小于等于0时表示无限制）
     */
    private long executeTimeOut;

    public ExecuteCondition() {
        this(false, false);
    }

    public ExecuteCondition(boolean useSingleton, boolean calExecuteTime) {
        this(useSingleton, calExecuteTime, 0);
    }

    public ExecuteCondition(boolean useSingleton, boolean calExecuteTime, long executeTimeOut) {
        super();
        this.useSingleton = useSingleton;
        this.calExecuteTime = calExecuteTime;
        this.executeTimeOut = executeTimeOut;
    }

    public boolean isUseSingleton() {
        return useSingleton;
    }

    public void setUseSingleton(boolean useSingleton) {
        this.useSingleton = useSingleton;
    }

    public boolean isCalExecuteTime() {
        return calExecuteTime;
    }

    public void setCalExecuteTime(boolean calExecuteTime) {
        this.calExecuteTime = calExecuteTime;
    }

    public long getExecuteTimeOut() {
        return executeTimeOut;
    }

    public void setExecuteTimeOut(long executeTimeOut) {
        this.executeTimeOut = executeTimeOut;
    }
}
