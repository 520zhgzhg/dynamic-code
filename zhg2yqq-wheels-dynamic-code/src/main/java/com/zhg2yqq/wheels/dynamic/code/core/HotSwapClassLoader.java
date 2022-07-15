/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import com.zhg2yqq.wheels.dynamic.code.IClassLoader;
import com.zhg2yqq.wheels.dynamic.code.exception.ClassLoadException;

/**
 * 为了多次载入执行类而加入的加载器 设计一个loadByte()方法将defineClass()方法开放出来，只有我们调用loadByte()方法时才使用自己的类加载器
 * 虚拟机调用HotSwapClassLoader时还是按照双亲委派模型使用loadClass方法进行类加载
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class HotSwapClassLoader extends ClassLoader implements IClassLoader {
    // 使用指定的父类加载器创建一个新的类加载器进行委派
    public HotSwapClassLoader() {
        super(HotSwapClassLoader.class.getClassLoader());
    }

    @Override
    public Class<?> loadByte(String name, byte[] classByte) throws ClassLoadException {
        try {
            return defineClass(null, classByte, 0, classByte.length);
        } catch (Exception e) {
            throw new ClassLoadException(e);
        }
    }
}
