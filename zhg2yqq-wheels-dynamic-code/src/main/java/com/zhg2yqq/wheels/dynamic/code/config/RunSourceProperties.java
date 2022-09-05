/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.config;

/**
 * Class执行器配置
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年8月31日
 */
public class RunSourceProperties extends BaseProperties {
    /**
     * 源码执行器的缓存容器大小
     */
    private int cacheSize = 100;

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
