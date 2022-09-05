/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.config;

/**
 * 统计耗时条件
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月14日
 */
public class BaseProperties {
    /**
     * 统计计算编译时间
     */
    private boolean calCompileTime;
    /**
     * 统计计算类方法执行时间
     */
    private boolean calExecuteTime;
    /**
     * 是否支持重复加载相同类
     * 默认true，新编译加载的类替换缓存的原始类，类与类之间的加载器不同
     * false，新编译的相同类重复加载时将报错，类与类之间的加载器相同
     */
    private boolean supportReload = true;
    /**
     * 方法执行超时时间，单位：毫秒（防止死循环占用，小于等于0时表示无限制）
     */
    private long executeTimeOut;

    public boolean isCalCompileTime() {
        return calCompileTime;
    }

    public void setCalCompileTime(boolean calCompileTime) {
        this.calCompileTime = calCompileTime;
    }

    public boolean isCalExecuteTime() {
        return calExecuteTime;
    }

    public void setCalExecuteTime(boolean calExecuteTime) {
        this.calExecuteTime = calExecuteTime;
    }

    public boolean isSupportReload() {
        return supportReload;
    }

    public void setSupportReload(boolean supportReload) {
        this.supportReload = supportReload;
    }

    public long getExecuteTimeOut() {
        return executeTimeOut;
    }

    public void setExecuteTimeOut(long executeTimeOut) {
        this.executeTimeOut = executeTimeOut;
    }
}
