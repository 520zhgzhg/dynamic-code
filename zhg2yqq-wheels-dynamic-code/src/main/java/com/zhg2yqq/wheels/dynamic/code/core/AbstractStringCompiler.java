/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import javax.tools.JavaCompiler;

import com.zhg2yqq.wheels.dynamic.code.IStringCompiler;
import com.zhg2yqq.wheels.dynamic.code.factory.AbstractCompilerFactory;
import com.zhg2yqq.wheels.dynamic.code.factory.StandardCompilerFactory;

/**
 * Java源码文本编译
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月26日
 */
public abstract class AbstractStringCompiler implements IStringCompiler {
    // java编译器
    private JavaCompiler compiler;

    public AbstractStringCompiler() {
        this(new StandardCompilerFactory());
    }

    public AbstractStringCompiler(AbstractCompilerFactory factory) {
        this.compiler = factory.getCompiler();
    }

    @Override
    public JavaCompiler getCompiler() {
        return compiler;
    }
}
