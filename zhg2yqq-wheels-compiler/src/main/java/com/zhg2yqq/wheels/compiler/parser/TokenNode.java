/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.parser;

import java.util.Iterator;

import com.zhg2yqq.wheels.compiler.tokenizer.Token;

/**
 * Token链表，用于生成
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年8月2日
 */
public class TokenNode implements Iterator<TokenNode> {
    private Token current;
    private TokenNode nextNode;

    public TokenNode(Token current) {
        this.current = current;
    }

    public TokenNode addNext(Token next) {
        this.nextNode = new TokenNode(next);
        return nextNode;
    }

    public void setNext(TokenNode nextNode) {
        this.nextNode = nextNode;
    }

    public Token getCurrent() {
        return current;
    }

    public TokenNode getNextNode() {
        return nextNode;
    }

    @Override
    public boolean hasNext() {
        return nextNode != null;
    }

    @Override
    public TokenNode next() {
        return nextNode;
    }
}
