/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.exception;

/**
 * 加载类异常
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月15日
 */
public class ClassLoadException extends BaseDynamicException {
    private static final long serialVersionUID = 1L;

    public ClassLoadException() {
        super("Load Class exception");
    }

    public ClassLoadException(Throwable e) {
        super("Load Class exception", e);
    }
}
