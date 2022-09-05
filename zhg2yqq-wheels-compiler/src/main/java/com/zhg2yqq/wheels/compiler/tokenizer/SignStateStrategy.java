/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

import com.zhg2yqq.wheels.compiler.exception.TokenException;
import com.zhg2yqq.wheels.compiler.util.Symbols;
import com.zhg2yqq.wheels.compiler.util.TokenUtils;

/**
 * 当前符号状态流转
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public class SignStateStrategy implements IStateMachineStrategy {
    @Override
    public State getSelfState() {
        return State.Sign;
    }

    @Override
    public State transform(char c, State prevState) throws TokenException {
        if (Symbols.inCharSet(c)) {
            // 符号
            return State.Sign;
        }
        if(TokenUtils.isNumber(c)) {
            // 数值
            return State.Number;
        }
        if(TokenUtils.isInIdentifier(c)) {
            // 标示符
            return State.Identifier;
        }
        if(c == '"') {
            // 字符串
            return State.String;
        }
        if(c == '\'') {
            // char
            return State.Char;
        }
        if(TokenUtils.isSpace(c)) {
            // 空格
            return State.Space;
        }
        throw new TokenException(c);
    }
}
