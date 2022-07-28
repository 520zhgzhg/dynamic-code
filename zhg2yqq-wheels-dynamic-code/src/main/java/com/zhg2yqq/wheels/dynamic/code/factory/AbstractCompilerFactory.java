/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.factory;

import javax.tools.JavaCompiler;

/**
 * 编译器工厂模式
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月26日
 */
public interface AbstractCompilerFactory {
    /**
     * 获取编译器
     * @return
     */
    public abstract JavaCompiler getCompiler();
}
