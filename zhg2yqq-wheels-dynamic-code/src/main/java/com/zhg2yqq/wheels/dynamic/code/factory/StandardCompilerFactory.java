/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.factory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.zhg2yqq.wheels.dynamic.code.core.StringJavaCompiler;
import com.zhg2yqq.wheels.dynamic.code.util.IOUtils;

/**
 * 标准编译器工厂
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月26日
 */
public class StandardCompilerFactory implements AbstractCompilerFactory {
    private Logger log = Logger.getLogger(StandardCompilerFactory.class.getName());
    private URL toolsUrl;

    public StandardCompilerFactory() {
        this(null);
    }

    public StandardCompilerFactory(URL toolsUrl) {
        super();
        this.toolsUrl = toolsUrl;
    }

    /**
     * 当toolsUrl不为null时，获取自定义工具包中的编译器；<br/>
     * 否则先尝试加载系统运行环境中JDK的编译器，如果未加载到编译器，则再尝试框架自带tools包（LITE不含）加载编译器。
     * 
     * @return .
     */
    @Override
    public JavaCompiler getCompiler() {
        if (toolsUrl != null) {
            try {
                log.info("加载tools.jar路径：" + toolsUrl);
                // 加载自定义tools中的编译器
                return loadJavaCompiler(toolsUrl);
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage(), e);
                throw new AssertionError("无法获取编译器，自定义路径：" + toolsUrl);
            }
        }

        // 获取java的编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler != null) {
            return compiler;
        }
        // 利用框架自带tools包，兜底创建编译器（LITE版无）
        // 创建临时暂存tools.jar
        String tempPath = System.getProperty("java.io.tmpdir");
        File tmpJar = new File(tempPath, "dynamic_tools_1.8.jat");
        URL loadUrl = null;
        try {
            loadUrl = tmpJar.toURI().toURL();
            log.info("加载tools.jar路径：" + loadUrl);
            InputStream in = StringJavaCompiler.class.getClassLoader()
                    .getResourceAsStream("lib/tools-1.8.jar");
            if (!tmpJar.exists()) {
                // 拷贝到临时目录
                FileOutputStream out = new FileOutputStream(tmpJar);
                IOUtils.copyByNIO(in, out, 2 << 10);
            }
            // 加载临时工具包
            return loadJavaCompiler(loadUrl);
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage(), e);
            if (tmpJar.exists()) {
                tmpJar.delete();
            }
            throw new AssertionError(
                    "无法获取编译器，默认路径：" + loadUrl + "。如果为LITE版，请务必保证运行环境有JDK或者配置自定义tools.jar路径。");
        }
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
