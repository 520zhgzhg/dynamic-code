/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

import com.zhg2yqq.wheels.compiler.exception.TokenException;
import com.zhg2yqq.wheels.compiler.util.Symbols;
import com.zhg2yqq.wheels.compiler.util.TokenUtils;

/**
 * 当前空格状态流转
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public class SpaceStateStrategy implements IStateMachineStrategy {
    @Override
    public State getSelfState() {
        return State.Space;
    }

    @Override
    public State transform(char c, State prevState) throws TokenException {
        if (TokenUtils.isSpace(c)) {
            return State.Space;
        }
        if(TokenUtils.isInIdentifier(c)) {
            // 标示符
            return State.Identifier;
        }
        if(Symbols.inCharSet(c)) {
            // 符号
            return State.Sign;
        }
        if(TokenUtils.isNumber(c)) {
            // 整数
            return State.Number;
        }
        if(c == '"') {
            // 字符串
            return State.String;
        }
        if(c == '\'') {
            // char
            return State.Char;
        }
        if (c == ';') {
            return State.Normal;
        }
        throw new TokenException(c);
    }
}
