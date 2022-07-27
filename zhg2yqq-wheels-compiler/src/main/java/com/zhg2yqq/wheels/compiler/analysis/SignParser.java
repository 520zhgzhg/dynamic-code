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
    // 根据长度分组存储
    private final static List<Set<String>> signSetList;
    // 所有运算符中可能出现的字符集合
    private final static Set<Character> signCharSet;
    private final static int MaxLength, MinLength;
    
    static {
        int maxLength = Integer.MIN_VALUE;
        int minLength = Integer.MAX_VALUE;
        signCharSet = new HashSet<>();
        for(String sign:signArray) {
            int length = sign.length();
            if(length > maxLength) {
                maxLength = length;
            }
            if(length < minLength) {
                minLength = length;
            }
            for(int i=0; i<length; ++i) {
                signCharSet.add(sign.charAt(i));
            }
        }
        signSetList = new ArrayList<>(maxLength - minLength + 1);
        for(int i=0; i< maxLength - minLength + 1; ++i) {
            signSetList.add(new HashSet<>());
        }
        for(String sign:signArray) {
            int length = sign.length();
            Set<String> signSet = signSetList.get(length - minLength);
            signSet.add(sign);
        }
        MaxLength = maxLength;
        MinLength = minLength;
    }
    
    static boolean inCharSet(char c) {
        return signCharSet.contains(c);
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
        length = Math.min(length, MaxLength);
        if(length >= MinLength) {
            for(int i=length - MinLength; i>=0; i--) {
                int matchLength = i + MinLength;
                HashSet<String> signSet = signSetList.get(i);
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
