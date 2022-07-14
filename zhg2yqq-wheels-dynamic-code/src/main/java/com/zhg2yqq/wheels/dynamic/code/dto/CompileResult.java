/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * 编译结果
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class CompileResult {
    private boolean success;
    private long compileTime;
    private String fullClassName;
    private ByteJavaFileObject fileObject;
    private DiagnosticCollector<JavaFileObject> diagnosticsCollector;

    public CompileResult(String fullClassName,
            DiagnosticCollector<JavaFileObject> diagnosticsCollector) {
        this.fullClassName = fullClassName;
        this.diagnosticsCollector = diagnosticsCollector;
    }

    /**
     * @return 编译信息(错误 警告)
     */
    public String getCompilerMessage() {
        StringBuilder sb = new StringBuilder();
        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector
                .getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            sb.append(diagnostic.toString()).append("\r\n");
        }
        return sb.toString();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public DiagnosticCollector<JavaFileObject> getDiagnosticsCollector() {
        return diagnosticsCollector;
    }
}
