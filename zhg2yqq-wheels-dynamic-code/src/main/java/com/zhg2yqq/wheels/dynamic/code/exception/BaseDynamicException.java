/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.exception;

/**
 * 基础异常
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月15日
 */
public class BaseDynamicException extends Exception {
    private static final long serialVersionUID = 1L;

    public BaseDynamicException() {
        super();
    }

    public BaseDynamicException(String msg, Throwable e) {
        super(msg, e);
    }

    public BaseDynamicException(String msg) {
        super(msg);
    }

    public BaseDynamicException(Throwable e) {
        super(e);
    }
}
