/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public enum State {
    // 初始状态
    Normal,
    // 关键字与标识符
    Identifier,
    // 数值
    Number, 
    Sign, 
//    Annotation,
    String, Char, 
//    RegEx, 
    Space
}
