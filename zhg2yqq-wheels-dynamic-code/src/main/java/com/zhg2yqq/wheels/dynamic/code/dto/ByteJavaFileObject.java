/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * 自定义一个编译之后的字节码对象
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class ByteJavaFileObject extends SimpleJavaFileObject {
    // 存放编译后的字节码
    private ByteArrayOutputStream outPutStream;

    public ByteJavaFileObject(String className, Kind kind) {
        super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension),
                kind);
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
