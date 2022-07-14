/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public interface IClassLoader {
    Class<?> loadByte(String name, byte[] classByte) throws ClassNotFoundException;
}
