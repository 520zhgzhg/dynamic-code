/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

/**
 * class方法执行结果
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月13日
 */
public class ExecuteResult {
    /**
     * 方法执行用时
     */
    private long runTakeTime;
    /**
     * 方法执行完后返回值
     */
    private Object returnVal;
    public ExecuteResult() {}
    public ExecuteResult(long runTakeTime, Object returnVal) {
        this.runTakeTime = runTakeTime;
        this.returnVal = returnVal;
    }
    
    public long getRunTakeTime() {
        return runTakeTime;
    }
    public Object getReturnVal() {
        return returnVal;
    }
}
