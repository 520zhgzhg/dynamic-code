/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * 自定义一个编译对象
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    // 等待编译的源码字段
    private String contents;
    // 存放编译后的字节码
    private ByteArrayOutputStream outPutStream;

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

    // StringJavaFileManage 编译之后的字节码输出会调用该方法（把字节码输出到outputStream）
    @Override
    public OutputStream openOutputStream() {
        outPutStream = new ByteArrayOutputStream();
        return outPutStream;
    }

    // 在类加载器加载的时候需要用到
    public byte[] getCompiledBytes() {
        return outPutStream.toByteArray();
    }
}
