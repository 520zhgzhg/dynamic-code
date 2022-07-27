/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.exception;

import java.util.List;
import java.util.StringJoiner;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 * 编译异常
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月15日
 */
public class CompileException extends BaseDynamicException {
    private static final long serialVersionUID = 1L;
    private String compileMessage;

    public CompileException() {
        super("Dynamic compilation exception");
    }

    public CompileException(String compileMessage) {
        super("Dynamic compilation exception");
        this.compileMessage = compileMessage;
    }

    public CompileException(String compileMessage, Throwable e) {
        super("Dynamic compilation exception", e);
        this.compileMessage = compileMessage;
    }

    public CompileException(DiagnosticCollector<JavaFileObject> diagnosticsCollector) {
        super("Dynamic compilation exception");
        this.compileMessage = this.formatCollector(diagnosticsCollector);
    }

    public CompileException(DiagnosticCollector<JavaFileObject> diagnosticsCollector, Throwable e) {
        super("Dynamic compilation exception", e);
        this.compileMessage = this.formatCollector(diagnosticsCollector);
    }

    @Override
    public String getMessage() {
        return compileMessage;
    }

    public String getCompileMessage() {
        return compileMessage;
    }

    protected String formatCollector(DiagnosticCollector<JavaFileObject> diagnosticsCollector) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector
                .getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            joiner.add(diagnostic.toString());
        }
        return joiner.toString();
    }
}
