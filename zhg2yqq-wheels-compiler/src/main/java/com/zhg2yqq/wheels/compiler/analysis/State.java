/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.analysis;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public enum State {
    // 初始状态
    Normal,
    Keyword, Number, Identifier, 
    Sign, Annotation,
    String, RegEx, Space
}
