/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import com.zhg2yqq.wheels.dynamic.code.exception.ClassLoadException;

/**
 * 类加载
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public interface IClassLoader {
    /**
     * 通过字节加载类
     * 
     * @param name 类全名
     * @param classByte 字节码
     * @return 类
     * @throws ClassLoadException .
     */
    Class<?> loadByte(String name, byte[] classByte) throws ClassLoadException;
}
