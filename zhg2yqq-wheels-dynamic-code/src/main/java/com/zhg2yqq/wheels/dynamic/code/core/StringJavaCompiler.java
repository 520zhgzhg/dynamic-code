/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.zhg2yqq.wheels.dynamic.code.util.IOUtils;

/**
 * 源码编译
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class StringJavaCompiler implements IStringCompiler {
    private Logger log = Logger.getLogger(StringJavaCompiler.class.getName());
    // 获取java的编译器
    private JavaCompiler compiler;

    public StringJavaCompiler() {
        this(null);
    }

    public StringJavaCompiler(URL jdkToolUrl) {
        URL loadUrl = jdkToolUrl;
        if (loadUrl == null) {
            // 获取java的编译器
            compiler = ToolProvider.getSystemJavaCompiler();
        } else {
            try {
                // 加载自定义tools中的编译器
                compiler = loadJavaCompiler(loadUrl);
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        }

        if (compiler == null) {
            // 利用框架自带tools包，兜底创建编译器
            try {
                // 创建临时暂存tools.jar
                InputStream in = StringJavaCompiler.class.getClassLoader()
                        .getResourceAsStream("lib/tools-1.8.jar");
                String tempPath = System.getProperty("java.io.tmpdir");
                File tmpJar = new File(tempPath, "dynamic_tools_1.8.jat");
                loadUrl = tmpJar.toURI().toURL();
                if (!tmpJar.exists()) {
                    FileOutputStream out = new FileOutputStream(tmpJar);
                    IOUtils.copyByNIO(in, out, 2 << 10);
                }
                // 加载临时工具包
                compiler = loadJavaCompiler(loadUrl);
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        }

        if (compiler == null) {
            throw new AssertionError("无法获取编译器，原始路径：" + jdkToolUrl + "，加载路径：" + loadUrl);
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
    public CompileResult compile(String fullClassName, String sourceCode, CalTimeDTO calTime)
        throws CompileException {
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

    @Override
    public JavaCompiler getCompiler() {
        return compiler;
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
        JavaCompiler.CompilationTask task = getCompiler().getTask(null, javaFileManager,
                diagnosticsCollector, null, null, Arrays.asList(javaFileObject));
        // TODO 后期扩展支持Processors，实现类似Lombok功能
//        task.setProcessors(processors);
        return task.call();
    }

    private JavaCompiler loadJavaCompiler(URL toolUrl) throws Exception {
        // 加载工具包
        URL[] urls = { toolUrl };
        URLClassLoader loader = new URLClassLoader(urls);
        // 获取编译工具类
        Class<?> javacTool = Class.forName("com.sun.tools.javac.api.JavacTool", true, loader);
        // 获取创建编译器方法
        Method create = javacTool.getMethod("create");
        // 获取编译器
        return (JavaCompiler) create.invoke(null);
    }
}
