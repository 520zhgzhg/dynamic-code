/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.zhg2yqq.wheels.compiler.analysis.Token;
import com.zhg2yqq.wheels.compiler.analysis.Token.Type;
import com.zhg2yqq.wheels.compiler.exception.TokenException;

/**
 * 词法分析器
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月27日
 */
public class Tokenizer {
    // 当前状态
    private State state;
    // 词
    private final LinkedList<Token> tokenBuffer = new LinkedList<>();
    // 
    private StringBuilder readBuffer = null;
    private Token endToken = null;
    private final Reader reader;
    public Tokenizer(Reader reader) {
        this.reader = reader;
        this.state = State.Normal;
    }
    
    /**
     * 词法状态机转换
     * @param c
     * @return
     * @throws TokenException 
     */
    private boolean readChar(char c) throws TokenException {
        boolean moveCursor = true;
        Type createType = null;
        if(state == State.Normal) {
            // 初始状态转换
            if(inIdentifierSetButNotRear(c)) {
                state = State.Identifier;
            } else if(SignParser.inCharSet(c)) {
                state = State.Sign;
            } else if(c ==  '@') {
                state = State.Annotation;
            } else if(c == '"' || c == '\'') {
                state = State.String;
            } else if(c == '`') {
                state = State.RegEx;
            } else if(c == ' ' || c == '\t') {
                state = State.Space;
            } else if(c == '\n') {
                createType = Type.NewLine;
            } else if(c == '\0') {
                createType = Type.EndSymbol;
            } else {
                throw new TokenException(c);
            }
            refreshBuffer(c);
        } else if(state == State.Identifier) {
            
        } else if(state == State.Sign) {
            if(SignParser.inCharSet(c)) {
                readBuffer.append(c);
            } else {
                List<String> list = SignParser.parse(readBuffer.toString());
                for(String signStr:list) {
                    createToken(Type.Sign, signStr);
                }
                createType = null;
                state = State.Normal;
                moveCursor = false;
            }
        } else if(state == State.Annotation) {
            
        } else if(state == State.String) {
            
        } else if(state == State.RegEx) {
        
        } else if(state == State.Space) {
            
        }
        
        if(createType != null) {
            createToken(createType);
        }
        return moveCursor;
    }
    
    private void createToken(Type type) {
        Token token = new Token(type, readBuffer.toString());
        tokenBuffer.addFirst(token);
        readBuffer = null;
    }
    
    private void createToken(Type type, String value) {
        Token token = new Token(type, value);
        tokenBuffer.addFirst(token);
        readBuffer = null;
    }
    
    private void refreshBuffer(char c) {
        readBuffer = new StringBuilder();
        readBuffer.append(c);
    }
    
    private boolean inIdentifierSetButNotRear(char c) {
        return (c >= 'a' && c <= 'z' ) || (c >='A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_');
    }

//    Token read() throws IOException, TokenException {
//        if(endToken != null) {
//            return endToken;
//        }
//        while(tokenBuffer.isEmpty()) {
//            int read = reader.read();
//            char c = (read == -1 ? '\0' : (char) read);
//            while(!readChar(c)) {}
//        }
//        Token token = tokenBuffer.removeLast();
//        if(token.type == Type.EndSymbol) {
//            endToken = token;
//        }
//        return token;
//    }
    
    Token read() throws IOException, TokenException {
        int read;
        do {
            // 读取单个字符
            read = reader.read();
            char c = (read == -1 ? '\0' : (char) read);
            // 进入状态机转换
            if(state == State.Normal) {
                // 初始状态转换
                if(inIdentifierSetButNotRear(c)) {
                    // 标示符
                    state = State.Identifier;
                } else if(SignParser.inCharSet(c)) {
                    // 符号
                    state = State.Sign;
                } else if(c ==  '@') {
                    // 注解
                    state = State.Annotation;
                } else if(c == '"' || c == '\'') {
                    // 字符串
                    state = State.String;
                } else if(c == '`') {
                    state = State.RegEx;
                } else if(c == ' ' || c == '\t') {
                    state = State.Space;
//                } else if(c == '\n') {
//                    createToken(Type.NewLine);
//                } else if(c == '\0') {
//                    createToken(Type.EndSymbol);
                } else {
                    throw new TokenException(c);
                }
                refreshBuffer(c);
            }
        } while(read != -1);
    }
    
    static class NormalState implements IStateMachineStrategy {

        @Override
        public State transform(char c) {
            // 初始状态转换
            if(inIdentifierSetButNotRear(c)) {
                // 标示符
                return State.Identifier;
            } else if(SignParser.inCharSet(c)) {
                // 符号
                return State.Sign;
            } else if(c ==  '@') {
                // 注解
                return State.Annotation;
            } else if(c == '"' || c == '\'') {
                // 字符串
                return State.String;
            } else if(c == '`') {
                return State.RegEx;
            } else if(c == ' ' || c == '\t') {
                return State.Space;
//            } else if(c == '\n') {
//                createToken(Type.NewLine);
//            } else if(c == '\0') {
//                createToken(Type.EndSymbol);
            } else {
                throw new TokenException(c);
            }
        }
        
    }
}
