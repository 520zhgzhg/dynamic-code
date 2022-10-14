/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

import com.zhg2yqq.wheels.compiler.exception.TokenException;
import com.zhg2yqq.wheels.compiler.util.Symbols;
import com.zhg2yqq.wheels.compiler.util.TokenUtils;

/**
 * 当前数值状态流转
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public class NumberStateStrategy implements IStateMachineStrategy {
    @Override
    public State getSelfState() {
        return State.Number;
    }

    @Override
    public State transform(char c, State prevState) throws TokenException {
        if (TokenUtils.isNumber(c)) {
            // 数值
            return State.Number;
        }
        if (c == '.') {
            // 数值
            return State.Number;
        }
        if (Symbols.inCharSet(c)) {
            return State.Sign;
        }
        if (TokenUtils.isSpace(c)) {
            return State.Space;
        }
        throw new TokenException(c);
    }
}
