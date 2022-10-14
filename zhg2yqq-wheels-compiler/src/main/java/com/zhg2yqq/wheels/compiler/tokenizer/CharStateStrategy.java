/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

import com.zhg2yqq.wheels.compiler.exception.TokenException;

/**
 * 当前char状态流转
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public class CharStateStrategy implements IStateMachineStrategy {
    @Override
    public State getSelfState() {
        return State.Char;
    }

    @Override
    public State transform(char c, State prevState) throws TokenException {
        if (c == '\'') {
            // char
            return State.Normal;
        }
        if (c == '\n' || c == '\0') {
            throw new TokenException(c);
        }
        return State.Char;
    }
}
