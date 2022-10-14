/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.parser;

import java.util.ArrayList;
import java.util.List;

import com.zhg2yqq.wheels.compiler.tokenizer.Token;

/**
 * 语法树节点
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年8月1日
 */
public abstract class AstTreeNode {
    public static enum Type {
        Program, FunctionDeclaration, VariableDeclaration, CallExpression,
    }

    private Type type;
    private Token token;
    private List<AstTreeNode> body = new ArrayList<>();

    protected AstTreeNode() {
        super();
    }

    protected AstTreeNode(Type type) {
        this(type, null);
    }

    protected AstTreeNode(Type type, List<AstTreeNode> body) {
        super();
        this.type = type;
        this.body = body;
    }

    protected void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void addTreeNode(AstTreeNode node) {
        this.body.add(node);
    }

    public List<AstTreeNode> getBody() {
        return body;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
