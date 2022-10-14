/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.parser;

import java.util.List;

import com.zhg2yqq.wheels.compiler.exception.ParseException;
import com.zhg2yqq.wheels.compiler.tokenizer.Token;
import com.zhg2yqq.wheels.compiler.util.Symbols;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年8月1日
 */
public class Parser {
    
    public AstTreeNode parse(List<Token> tokens) {
        int i;
        i = 0;
        i++;
        i+=1;
        i+1;
        AstTreeNode root = new ProgramNode();
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }
        TokenNode tokenNode = this.getTokenNode(tokens);
        TokenNode currentNode = tokenNode;
        do {
            Token token = currentNode.getCurrent();
            Token.Type type = token.getType();
            if (type == Type.Identifier) {
                if (tokenNode.hasNext()) {
                    tokenNode = tokenNode.getNextNode();
                    Token next = tokenNode.getCurrent();
                    if (next.getType() == Type.Sign) {
                        if (next.getValue() == "(") {
                            // 方法
                            FunctionDeclarationNode
                        }
                    }
                }
            } else if (type == Type.Keyword) {

            } else if (type == Type.Number) {
                // error
            } else if (type == Type.Sign) {
                // 区分处理
            } else if (type == Type.String) {
                // error
            } else if (type == Type.Space) {
                continue;
            } else if (type == Type.NewLine) {
                
            } else if (type == Type.EndSymbol) {
                break;
            }
        } while(tokenLinkedList.hasNext());
        return root;
    }
    
    private AstTreeNode parseIdentifier(TokenNode node) throws ParseException {
        // 判断区分是调用方法还是变量赋值
        while (node.hasNext()) {
            TokenNode nextNode = node.next();
            Token nextToken = nextNode.getCurrent();
            if (nextToken.getType() == Token.Type.Space) {
                continue;
            }
            if (nextToken.getType() == Token.Type.Sign) {
                if ("(".equals(nextToken.getValue())) {
                    // 调用方法
                    CallExpressionNode callExpressionNode = new CallExpressionNode();
                    while ((nextNode = nextNode.next()) != null) {
                        // 获取方法参数
                        nextToken = nextNode.getCurrent();
                        if (nextToken.getType() == Token.Type.Space || nextToken.getType() == Token.Type.NewLine) {
                            continue;
                        }
                        if (nextToken.getType() == Token.Type.Sign) {
                            if (")".equals(nextToken.getValue())) {
                                // 方法参数关闭
                                while ((nextNode = nextNode.next()) != null) {
                                    if (nextToken.getType() == Token.Type.Space) {
                                        continue;
                                    }
                                    if (nextToken.getType() == Token.Type.Sign && ";".equals(nextToken.getValue())) {
                                        node.setNext(nextNode);
                                        // 调用方法结束
                                        return callExpressionNode;
                                    }
                                    throw new ParseException(node.getCurrent());
                                }
                            }
                            throw new ParseException(node.getCurrent());
                        }
                        if (nextToken.getType() == Token.Type.Identifier || nextToken.getType() == Token.Type.Number || nextToken.getType() == Token.Type.String) {
                            // TODO 构造参数
                            while ((nextNode = nextNode.next()) != null) {
                                
                            }
                            throw new ParseException(node.getCurrent());
                            // 参数
//                            VariableDeclarationNode paramNode = new VariableDeclarationNode();
//                            paramNode.setToken(nextToken);
//                            callExpressionNode.addTreeNode(paramNode);
//                            if (nextNode.hasNext()) {
//                                
//                            }
                        } else {
                            throw new ParseException(node.getCurrent());
                        }
                    }
                    throw new ParseException(node.getCurrent());
                } else if (Symbols.isAssignmentSymbol(nextToken.getValue())) {
                    VariableDeclarationNode variableDeclarationNode = new VariableDeclarationNode();
                }
                throw new ParseException(node.getCurrent());
            }
            throw new ParseException(node.getCurrent());
        }
        throw new ParseException(node.getCurrent());
    }
    
    private AstTreeNode parseKeyword(TokenNode node) {
    }
    
    
    
    private TokenNode getTokenNode(List<Token> tokens) {
        TokenNode firstTokenNode = null;
        TokenNode node = null;
        for (Token token : tokens) {
            if (firstTokenNode == null) {
                firstTokenNode = new TokenNode(token);
                node = firstTokenNode;
                continue;
            }
            node = node.addNext(token);
        }
        return firstTokenNode;
    }
}
