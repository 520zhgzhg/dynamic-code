/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.zhg2yqq.wheels.compiler.exception.TokenException;
import com.zhg2yqq.wheels.compiler.tokenizer.Token;
import com.zhg2yqq.wheels.compiler.tokenizer.Tokenizer;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月30日
 */
public class TokenizerTest {
    public static void main(String[] args) throws TokenException, IOException {
        String code = "1 + 2 * '测试'";
        StringReader reader = new StringReader(code);
        Tokenizer tokenizer = new Tokenizer(reader);
        List<Token> tokens = tokenizer.read();
        for(Token token : tokens) {
            System.out.println(token.toString());
        }
    }
}
