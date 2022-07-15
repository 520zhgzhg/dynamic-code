/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

/**
 * 统计耗时条件
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月14日
 */
public class CalTimeDTO {
    /**
     * 统计计算编译时间
     */
    private boolean calCompileTime;
    /**
     * 统计计算类方法执行时间
     */
    private boolean calExecuteTime;

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
}
