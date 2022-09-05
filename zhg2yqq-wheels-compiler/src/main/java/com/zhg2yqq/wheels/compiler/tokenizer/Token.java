/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.tokenizer;

/**
 * 词法-词属性
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月27日
 */
public class Token {
    public static enum Type {
        // 关键字
        Keyword,
        // 数值
        Number,
        // 标示符（定义的变量、函数定义中的函数名和参数名、被调用函数的函数名等等）
        Identifier,
        // 符号（+、-、*、/、=、;、.等）
        Sign,
//        // 注解
//        Annotation,
        // 字符串
        String,
//        // 正则表达式
//        RegEx,
        // 空格
        Space,
        // 换行符
        NewLine,
        // 终止符
        EndSymbol;
    }
    // 类型
    final Type type;
    // 语素
    final String value;
//    final int line;
//    final int column;

    public Token(Type type, String value) {
//        // 对编译器来说，Keyword、Number、Identifier高度相同，可统称为Identifier
//        if (Type.Identifier == type) {
//            char firstChar = value.charAt(0);
//            if(firstChar >= "0" & firstChar < "9") {
//                type = Type.Number;
//            } else if(keywordsSet.contains(value)){
//                type = Type.Keyword;
//            }
//        }
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token [type=" + type + ", value=" + value + "]";
    }
}
