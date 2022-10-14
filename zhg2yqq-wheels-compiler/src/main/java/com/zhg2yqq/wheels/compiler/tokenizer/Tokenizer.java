/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import com.zhg2yqq.wheels.compiler.exception.TokenException;
import com.zhg2yqq.wheels.compiler.tokenizer.Token.Type;
import com.zhg2yqq.wheels.compiler.util.Keywords;
import com.zhg2yqq.wheels.compiler.util.Symbols;
import com.zhg2yqq.wheels.compiler.util.TokenUtils;

/**
 * 词法分析
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月29日
 */
public class Tokenizer {
    // 各个状态机策略
    private static final EnumMap<State, IStateMachineStrategy> STATE_STRATEGIES = new EnumMap<>(
            State.class);
    static {
        putStateStrategy(new NormalStateStrategy());
        putStateStrategy(new CharStateStrategy());
        putStateStrategy(new NumberStateStrategy());
        putStateStrategy(new SignStateStrategy());
        putStateStrategy(new SpaceStateStrategy());
        putStateStrategy(new StringStateStrategy());
        putStateStrategy(new IdentifierStateStrategy());
    }

    private static void putStateStrategy(IStateMachineStrategy state) {
        STATE_STRATEGIES.put(state.getSelfState(), state);
    }

    private Reader reader;
    private State state;
    private StringBuilder cacheToken = new StringBuilder();
    private List<Token> tokens = new LinkedList<>();

    public Tokenizer(Reader reader) {
        this.reader = reader;
        this.state = State.Normal;
    }

    private void read(char c) throws TokenException {
        State prevState = state;
        // 获取当前状态机处理状态转换
        IStateMachineStrategy stateStrategy = STATE_STRATEGIES.get(state);

        if (c == '\n') {
            this.addToken(prevState);
            tokens.add(new Token(Type.NewLine, null));
            state = State.Normal;
            return;
        }
        // 获取转换后的状态
        state = stateStrategy.transform(c, prevState);
        if (state != prevState) {
            if (state == State.Normal) {
                cacheToken.append(c);
            }
            // 状态发生改变，将之前缓存中的字符串取出(并清空缓存)，根据转换前的状态创建Token
            this.addToken(prevState);
        }
        // String、Char因关闭符导致的状态转换成Normal（;符号状态会转变成Sign）
        if (state != State.Normal) {
            cacheToken.append(c);
        }
    }
    
    private void addToken(State state) throws TokenException {
        String value = this.getCacheToken();
        if (state == State.Sign) {
            List<String> signs = Symbols.parse(value);
            for (String sign : signs) {
                tokens.add(new Token(Type.Sign, sign));
            }
        } else if (state == State.Identifier) {
            Type type = Keywords.isKeyword(value) ? Type.Keyword : Type.Identifier;
            tokens.add(new Token(type, value));
        } else if (state == State.Char || state == State.String) {
            tokens.add(new Token(Type.String, value));
        } else if (state == State.Number) {
            if (!TokenUtils.isNumber(value)) {
                throw new TokenException(value);
            }
            tokens.add(new Token(Type.Number, value));
        } else if (state == State.Space) {
            tokens.add(new Token(Type.Space, value));
        }
    }
    
    private String getCacheToken() {
        String token = cacheToken.toString();
        cacheToken = new StringBuilder();
        return token;
    }

    public synchronized List<Token> read() throws IOException, TokenException {
        // 防止重复读取，如已分词完毕，直接返回分词后结果
        if (!tokens.isEmpty()) {
            return tokens;
        }
        int readChar = -1;
        while ((readChar = reader.read()) != -1) {
            char c = (char) readChar;
            read(c);
        }
        if (cacheToken.length() > 0) {
            this.addToken(state);
        }
        tokens.add(new Token(Type.EndSymbol, null));
        return tokens;
    }
}
