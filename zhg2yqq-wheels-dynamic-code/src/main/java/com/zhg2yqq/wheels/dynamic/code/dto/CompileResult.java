/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

/**
 * 编译结果
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class CompileResult {
    private long compileTime;
    private String fullClassName;
    private ByteJavaFileObject fileObject;

    public CompileResult(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public long getCompileTime() {
        return compileTime;
    }

    public void setCompileTime(long compileTime) {
        this.compileTime = compileTime;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public ByteJavaFileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(ByteJavaFileObject fileObject) {
        this.fileObject = fileObject;
    }
}
