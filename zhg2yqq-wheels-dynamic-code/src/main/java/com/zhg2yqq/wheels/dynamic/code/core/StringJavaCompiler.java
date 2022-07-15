/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.zhg2yqq.wheels.dynamic.code.IStringCompiler;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;
import com.zhg2yqq.wheels.dynamic.code.dto.StringJavaFileObject;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;

/**
 * 源码编译
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class StringJavaCompiler implements IStringCompiler {
    // 获取java的编译器
    private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    static {
        if (compiler == null) {
            try {
                Class<?> javacTool = Class.forName("com.sun.tools.javac.api.JavacTool");
                Method create = javacTool.getMethod("create");
                compiler = (JavaCompiler) create.invoke(null);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }

    /**
     * 编译字符串源代码,编译失败在 diagnosticsCollector 中获取提示信息
     *
     * @param fullClassName class全名
     * @param sourceCode 源码字符串内容
     * @param calTime 统计耗时条件
     * @return CompileResult 编译后的结果
     * @throws CompileException 
     */
    @Override
    public CompileResult compile(String fullClassName, String sourceCode, CalTimeDTO calTime) throws CompileException {
        long compilerTakeTime = -1;
        boolean compileSuccess;
        // 存放编译过程中输出的信息
        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
        CompileResult result = new CompileResult(fullClassName);
        if (calTime.isCalCompileTime()) {
            long startTime = System.currentTimeMillis();
            // 编译成字节，并将字节对象放入result
            compileSuccess = compile(sourceCode, diagnosticsCollector, result);
            // 设置编译耗时(单位ms)
            compilerTakeTime = System.currentTimeMillis() - startTime;
        } else {
            // 编译成字节，并将字节对象放入result
            compileSuccess = compile(sourceCode, diagnosticsCollector, result);
        }
        result.setCompileTime(compilerTakeTime);
        if (compileSuccess) {
            return result;
        }
        throw new CompileException(diagnosticsCollector);
    }

    /**
     * 核心编译
     * 
     * @param sourceCode 源码
     * @param 编译错误信息
     * @param result 结果
     * @return 是否编译成功
     */
    protected boolean compile(String sourceCode,
                            DiagnosticCollector<JavaFileObject> diagnosticsCollector,
                            CompileResult result) {
        // 标准的内容管理器,更换成自己的实现，覆盖部分方法
        StandardJavaFileManager standardFileManager = compiler
                .getStandardFileManager(diagnosticsCollector, null, null);
        JavaFileManager javaFileManager = new StringJavaFileManager(standardFileManager, result);
        // 构造源代码对象
        JavaFileObject javaFileObject = new StringJavaFileObject(result.getFullClassName(),
                sourceCode);
        // 获取一个编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, javaFileManager,
                diagnosticsCollector, null, null, Arrays.asList(javaFileObject));
        // TODO 后期扩展支持Processors，实现类似Lombok功能
//        task.setProcessors(processors);
        return task.call();
    }
}
