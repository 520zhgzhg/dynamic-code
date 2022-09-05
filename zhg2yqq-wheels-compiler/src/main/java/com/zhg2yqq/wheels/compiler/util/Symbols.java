/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月27日
 */
public class Symbols {
    // 所有符号
    private static final Set<String> SYMBOLS = new HashSet<>();
    // 算数运算符
    private static final Set<String> ARITHMETIC_SYMBOLS = new HashSet<>();
    // 赋值运算
    private static final Set<String> ASSIGNMENT_SYMBOLS = new HashSet<>();
    // 关系运算符
    private static final Set<String> JUDGEMENT_SYMBOLS = new HashSet<>();
    // 位运算
    private static final Set<String> BIT_SYMBOLS = new HashSet<>();
    // 逻辑运算符
    private static final Set<String> LOGIC_SYMBOLS = new HashSet<>();
    // 三元
    private static final Set<String> TERNARY_SYMBOLS = new HashSet<>();
    
    // 所有运算符中可能出现的字符集合
    private static final Set<Character> SYMBOL_CHAR_SET = new HashSet<>();
    
    static {
        ARITHMETIC_SYMBOLS.add("+");
        ARITHMETIC_SYMBOLS.add("-");
        ARITHMETIC_SYMBOLS.add("*");
        ARITHMETIC_SYMBOLS.add("/");
        ARITHMETIC_SYMBOLS.add("%");
        SYMBOLS.addAll(ARITHMETIC_SYMBOLS);
        
        ASSIGNMENT_SYMBOLS.add("++");
        ASSIGNMENT_SYMBOLS.add("--");
        ASSIGNMENT_SYMBOLS.add("+=");
        ASSIGNMENT_SYMBOLS.add("-=");
        ASSIGNMENT_SYMBOLS.add("*=");
        ASSIGNMENT_SYMBOLS.add("/=");
        ASSIGNMENT_SYMBOLS.add("%=");
        ASSIGNMENT_SYMBOLS.add("&=");
        ASSIGNMENT_SYMBOLS.add("|=");
        ASSIGNMENT_SYMBOLS.add("^=");
        ASSIGNMENT_SYMBOLS.add("=");
        SYMBOLS.addAll(ASSIGNMENT_SYMBOLS);

        JUDGEMENT_SYMBOLS.add(">");
        JUDGEMENT_SYMBOLS.add("<");
        JUDGEMENT_SYMBOLS.add(">=");
        JUDGEMENT_SYMBOLS.add("<=");
        JUDGEMENT_SYMBOLS.add("!=");
        JUDGEMENT_SYMBOLS.add("==");
        SYMBOLS.addAll(JUDGEMENT_SYMBOLS);
        
        BIT_SYMBOLS.add("&");
        BIT_SYMBOLS.add("|");
        BIT_SYMBOLS.add("^");
        BIT_SYMBOLS.add("~");
        BIT_SYMBOLS.add("<<");
        BIT_SYMBOLS.add(">>");
        SYMBOLS.addAll(BIT_SYMBOLS);
        
        LOGIC_SYMBOLS.add("&&");
        LOGIC_SYMBOLS.add("||");
        LOGIC_SYMBOLS.add("!");
        SYMBOLS.addAll(BIT_SYMBOLS);
        
        TERNARY_SYMBOLS.add("?");
        TERNARY_SYMBOLS.add(":");
        SYMBOLS.addAll(TERNARY_SYMBOLS);
        
        SYMBOLS.add(".");
        SYMBOLS.add(",");
        SYMBOLS.add(";");
        SYMBOLS.add("(");
        SYMBOLS.add(")");
        SYMBOLS.add("[");
        SYMBOLS.add("]");
        SYMBOLS.add("{");
        SYMBOLS.add("}");
        
        for (String symbol : SYMBOLS) {
            int length = symbol.length();
            for (int i = 0; i < length; i++) {
                SYMBOL_CHAR_SET.add(symbol.charAt(i));
            }
        }
    }

    public static boolean inCharSet(char c) {
        return SYMBOL_CHAR_SET.contains(c);
    }

    public static boolean isSymbol(String str) {
        return SYMBOLS.contains(str);
    }

    public static boolean isAssignmentSymbol(String str) {
        return ASSIGNMENT_SYMBOLS.contains(str);
    }

    public static List<String> parse(String str) {
        List<String> signs = new LinkedList<>();
        int start = 0;
        int end = start + 1;
        int len = str.length();
        do {
            String cur = str.substring(start, end);
            // 判断未到尾部，且当前字符串加下一个字符而成的字符串还是符号
            boolean continueNextChar = end < len && isSymbol(str.substring(start, end + 1));
            if (!continueNextChar) {
                // 加下一个字符不是符号，则把当前字符串存入集合
                signs.add(cur);
                // 定义下个字符串下标开始的位置
                start = end;
            }
            end++;
        } while (end <= len);
        return signs;
    }
}
