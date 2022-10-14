/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 关键字
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年8月3日
 */
public class Keywords {
    // 所有关键字
    private static final Set<String> KEYWORDS = new HashSet<>();
    // 基础数据类型
    private static final Set<String> BASIC_KEYWORDS = new HashSet<>();
    // 判断关键字
    private static final Set<String> JUDGEMENT_KEYWORDS = new HashSet<>();
    // 循环关键字
    private static final Set<String> LOOP_KEYWORDS = new HashSet<>();
    static {
        BASIC_KEYWORDS.add("byte");
        BASIC_KEYWORDS.add("char");
        BASIC_KEYWORDS.add("short");
        BASIC_KEYWORDS.add("int");
        BASIC_KEYWORDS.add("long");
        BASIC_KEYWORDS.add("float");
        BASIC_KEYWORDS.add("double");
        BASIC_KEYWORDS.add("boolean");
        BASIC_KEYWORDS.add("str");
        BASIC_KEYWORDS.add("decimal");
        BASIC_KEYWORDS.add("object");
        
        KEYWORDS.addAll(BASIC_KEYWORDS);
        
        JUDGEMENT_KEYWORDS.add("if");
        JUDGEMENT_KEYWORDS.add("elsif");
        JUDGEMENT_KEYWORDS.add("else");
//        JUDGEMENT_KEYWORDS.add("when");
        
        KEYWORDS.addAll(JUDGEMENT_KEYWORDS);
        
        LOOP_KEYWORDS.add("do");
        LOOP_KEYWORDS.add("while");
//        LOOP_KEYWORDS.add("begin");
//        LOOP_KEYWORDS.add("until");
//        LOOP_KEYWORDS.add("end");
        LOOP_KEYWORDS.add("for");
        
        KEYWORDS.addAll(LOOP_KEYWORDS);
        
        
        KEYWORDS.add("try");
        KEYWORDS.add("catch");
        KEYWORDS.add("finally");
//        KEYWORDS.add("def");
//        KEYWORDS.add("var");
//        KEYWORDS.add("this");
        KEYWORDS.add("null");
//        KEYWORDS.add("throw");
        KEYWORDS.add("break");
        KEYWORDS.add("continue");
        KEYWORDS.add("return");
    }

    public static boolean isBasicKeyword(String str) {
        return BASIC_KEYWORDS.contains(str);
    }

    public static boolean isJudgementKeyword(String str) {
        return JUDGEMENT_KEYWORDS.contains(str);
    }

    public static boolean isLoopKeyword(String str) {
        return LOOP_KEYWORDS.contains(str);
    }

    public static boolean isKeyword(String str) {
        return KEYWORDS.contains(str);
    }
}
