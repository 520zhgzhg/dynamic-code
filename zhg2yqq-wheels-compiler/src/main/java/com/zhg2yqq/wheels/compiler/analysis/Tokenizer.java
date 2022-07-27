/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

import com.zhg2yqq.wheels.compiler.analysis.Token.Type;
import com.zhg2yqq.wheels.compiler.exception.TokenException;

/**
 * 词法分析器
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月27日
 */
public class Tokenizer {
    // 状态机中的状态
    public static enum State {
        // 初始状态
        Normal,
        Keyword, Number, Identifier, 
        Sign, Annotation,
        String, RegEx, Space
    }

    // 当前状态
    private State state;
    // 词
    private final LinkedList<Token> tokenBuffer = new LinkedList<>();
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
    
    private void refreshBuffer(char c) {
        readBuffer = new StringBuilder();
        readBuffer.append(c);
    }
    
    private boolean inIdentifierSetButNotRear(char c) {
        return (c >= 'a' && c <= 'z' ) || (c >='A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_');
    }

    Token read() throws IOException, TokenException {
        if(endToken != null) {
            return endToken;
        }
        while(tokenBuffer.isEmpty()) {
            int read = reader.read();
            char c = (read == -1 ? '\0' : (char) read);
            while(!readChar(c)) {}
        }
        Token token = tokenBuffer.removeLast();
        if(token.type == Type.EndSymbol) {
            endToken = token;
        }
        return token;
    }
}
