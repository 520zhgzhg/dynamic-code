/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.exception;

/**
 * 类执行异常
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月15日
 */
public class ExecuteException extends BaseDynamicException {
    private static final long serialVersionUID = 1L;
    /**
     * 执行代码时，代码中抛出的异常
     */
    private Throwable sourceCause;

    public ExecuteException() {
        super("Class execute exception");
    }

    public ExecuteException(Throwable e) {
        super("Class execute exception", e);
    }

    public ExecuteException(Throwable e, Throwable sourceCause) {
        super("Class execute exception", e);
        this.sourceCause = sourceCause;
    }

    public Throwable getSourceCause() {
        return sourceCause;
    }
}
