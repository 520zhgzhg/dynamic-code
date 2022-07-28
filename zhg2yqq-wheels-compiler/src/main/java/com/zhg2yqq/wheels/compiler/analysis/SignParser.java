/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.zhg2yqq.wheels.compiler.exception.TokenException;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月27日
 */
public class SignParser {
    // 符号集
    private static String[] signArray = new String[] {
        // 算数运算符
        "+", "-", "*", "/", "%", "++", "--",
        // 关系运算
        ">", "<", ">=", "<=", "!=", "==",
        // 赋值运算
        "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "=",
        // 位运算
        "&", "|", "^","~","<<",">>",
        // 短路逻辑运算
        "&&", "||", "!",
        // 三元
        "?", ":",
        ".", ",", ";",
        "(", ")", "[", "]", "{", "}"
    };
    // 所有运算符中可能出现的字符集合
    private static final Set<Character> SIGN_CHAR_SET = new HashSet<>();
    // 根据长度分组存储
    private static final List<Set<String>> SIGN_SET_LIST;
    // 所有符号里最大长度与最小长度
    private static int maxLength = Integer.MIN_VALUE;
    private static int minLength = Integer.MAX_VALUE;
    
    static {
        for(String sign:signArray) {
            int length = sign.length();
            if(length > maxLength) {
                maxLength = length;
            }
            if(length < minLength) {
                minLength = length;
            }
            for(int i=0; i<length; ++i) {
                SIGN_CHAR_SET.add(sign.charAt(i));
            }
        }
        
        // 根据符号长度分组
        int lenGroupSize = maxLength - minLength + 1;
        SIGN_SET_LIST = new ArrayList<>(lenGroupSize);
        for(int i=0; i < lenGroupSize; i++) {
            SIGN_SET_LIST.add(new HashSet<>());
        }
        for(String sign:signArray) {
            int length = sign.length();
            Set<String> signSet = SIGN_SET_LIST.get(length - minLength);
            signSet.add(sign);
        }
    }
    
    static boolean inCharSet(char c) {
        return SIGN_CHAR_SET.contains(c);
    }
    
    static List<String> parse(String str)  {
        List<String> rsContainer = new LinkedList<>(); 
        int startIndex = 0;
        while(startIndex < str.length()) {
            String matchStr = match(startIndex, str);
            if(matchStr == null) {
                throw new TokenException(str.substring(startIndex));
            } else {
                rsContainer.add(matchStr);
                startIndex += matchStr.length();
            }
        }
        return rsContainer;
    }
    
    private static String match(int startIndex, String str) {
        String matchStr = null;
        int length = str.length() - startIndex;
        length = Math.min(length, maxLength);
        if(length >= minLength) {
            for(int i=length - minLength; i>=0; i--) {
                int matchLength = i + minLength;
                Set<String> signSet = SIGN_SET_LIST.get(i);
                matchStr = str.substring(startIndex, startIndex + matchLength);
                if(signSet.contains(matchStr)) {
                    break;
                }
                matchStr = null;
            }
        }
        return matchStr;
    }
}
