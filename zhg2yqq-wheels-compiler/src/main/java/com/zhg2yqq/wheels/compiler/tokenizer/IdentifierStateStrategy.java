/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

import com.zhg2yqq.wheels.compiler.exception.TokenException;
import com.zhg2yqq.wheels.compiler.util.Symbols;
import com.zhg2yqq.wheels.compiler.util.TokenUtils;

/**
 * 当前关键字标识符状态流转
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public class IdentifierStateStrategy implements IStateMachineStrategy {
    @Override
    public State getSelfState() {
        return State.Identifier;
    }

    @Override
    public State transform(char c, State prevState) throws TokenException {
        if(TokenUtils.isInIdentifier(c) || TokenUtils.isNumber(c)) {
            // 标示符
            return State.Identifier;
        }
        if (Symbols.inCharSet(c)) {
            // 符号
            return State.Sign;
        }
        if (c == '"') {
            // 字符串
            return State.String;
        }
        if (c == '\'') {
            // 字符串
            return State.Char;
        }
        if(TokenUtils.isSpace(c)) {
            // 空格
            return State.Space;
        }
        if (c == ';') {
            return State.Normal;
        }
        throw new TokenException(c);
    }
}
