/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.exception;

import com.zhg2yqq.wheels.compiler.tokenizer.Token;

/**
 * 语法分析异常
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月27日
 */
public class ParseException extends CompileException {
    private static final long serialVersionUID = 1L;
    private Token token;
    
    public ParseException(Token token) {
        this.token = token;
    }
}
