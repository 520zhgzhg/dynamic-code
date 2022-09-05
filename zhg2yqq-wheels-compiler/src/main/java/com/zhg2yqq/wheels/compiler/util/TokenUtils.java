/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.util;

import java.util.regex.Pattern;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public class TokenUtils {
    private static final Pattern PATTERN_NUMBER = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
    private static final Pattern PATTERN_INTEGER = Pattern.compile("^(\\-|\\+)?\\d+$");
    private static final Pattern PATTERN_DECIMAL = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)$");

    public static boolean isInIdentifier(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= 0x4E00 && c <= 0x9FA5)
                || (c == '_');
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isNumber(String str) {
        return PATTERN_NUMBER.matcher(str).matches();
    }

    public static boolean isSpace(char c) {
        return c == ' ' || c == '\t';
    }
}
