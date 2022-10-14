/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.parser;

/**
 * 定义变量
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年8月1日
 */
public class VariableDeclarationNode extends AstTreeNode {

    public VariableDeclarationNode() {
        super(AstTreeNode.Type.VariableDeclaration);
    }

}
