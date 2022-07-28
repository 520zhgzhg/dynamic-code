/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Class与实例
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月23日
 */
public class ClassBean {
    private Class<?> clazz;
    private AtomicReference<Object> instance;

    public ClassBean() {
        this(null);
    }

    public ClassBean(Class<?> clazz) {
        this(clazz, null);
    }

    public ClassBean(Class<?> clazz, Object instance) {
        super();
        this.clazz = clazz;
        this.instance = new AtomicReference<>(instance);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object getInstance() {
        return instance.get();
    }

    public AtomicReference<Object> getInstanceReference() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = new AtomicReference<>(instance);
    }
}
