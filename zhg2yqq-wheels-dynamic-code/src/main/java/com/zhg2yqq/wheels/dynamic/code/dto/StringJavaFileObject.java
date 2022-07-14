/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * 自定义一个字符串的源码对象
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    // 等待编译的源码字段
    private String contents;

    // java源代码 => StringJavaFileObject对象 的时候使用
    public StringJavaFileObject(String className, String contents) {
        super(URI.create(
                "string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension),
                Kind.SOURCE);
        this.contents = contents;
    }

    // 字符串源码会调用该方法
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return contents;
    }
}
