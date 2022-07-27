/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.exception;

/**
 * 词法分析异常
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月27日
 */
public class TokenException extends CompileException {
    private static final long serialVersionUID = 1L;
    private Character character;
    
    public TokenException(Character character) {
        this.character = character;
    }
}
