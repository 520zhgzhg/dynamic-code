/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.analysis;

import com.zhg2yqq.wheels.compiler.exception.TokenException;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public class SignStateStrategy implements IStateMachineStrategy {
    @Override
    public State getSelfState() {
        return State.Sign;
    }

    @Override
    public State transform(char c) throws TokenException {
        // 初始状态转换
        if(TokenUtils.inIdentifierSetButNotRear(c)) {
            // 标示符
            return State.Identifier;
        } else if(SignParser.inCharSet(c)) {
            // 符号
            return State.Sign;
        } else if(c == '"' || c == '\'') {
            // 字符串
            return State.String;
        } else if(c == ' ' || c == '\t') {
            return State.Space;
//        } else if(c == '\n') {
//            createToken(Type.NewLine);
//        } else if(c == '\0') {
//            return State.
        } else {
            throw new TokenException(c);
        }
    }
}
