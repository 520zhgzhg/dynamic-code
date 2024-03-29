/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.zhg2yqq.wheels.dynamic.code.dto.ByteJavaFileObject;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;

/**
 * 自定义一个JavaFileManage来控制编译之后字节码的输出位置
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class StringJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private CompileResult result;
    public StringJavaFileManager(JavaFileManager fileManager, CompileResult result) {
        super(fileManager);
        this.result = result;
    }
    
    // 获取输出的文件对象，它表示给定位置处指定类型的指定类。
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className,
                                               JavaFileObject.Kind kind, FileObject sibling)
        throws IOException {
        ByteJavaFileObject javaFileObject = new ByteJavaFileObject(className, kind);
        result.setFileObject(javaFileObject);
        return javaFileObject;
    }
}
