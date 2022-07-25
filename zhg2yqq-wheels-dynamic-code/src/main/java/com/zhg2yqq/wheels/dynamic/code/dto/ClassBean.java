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
public class ClassBean<T> {
    private Class<T> clazz;
    private AtomicReference<T> instance;

    public ClassBean() {
        this(null);
    }

    public ClassBean(Class<T> clazz) {
        this(clazz, null);
    }

    public ClassBean(Class<T> clazz, T instance) {
        super();
        this.clazz = clazz;
        this.instance = new AtomicReference<>(instance);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getInstance() {
        return instance.get();
    }

    public AtomicReference<T> getInstanceReference() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = new AtomicReference<>(instance);
    }
}
