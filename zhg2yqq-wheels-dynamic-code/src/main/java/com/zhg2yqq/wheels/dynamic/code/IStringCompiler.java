/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import com.zhg2yqq.wheels.dynamic.code.config.BaseProperties;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;

/**
 * 源码编译器
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public interface IStringCompiler {
    /**
     * 编译源码
     * 
     * @param fullClassName 类全名
     * @param sourceCode 源码
     * @param properties 配置
     * @return 成功编译结果
     * @throws CompileException .
     */
    CompileResult compile(String fullClassName, String sourceCode, BaseProperties properties)
        throws CompileException;
    
//    /**
//     * 获取编译器
//     * @return
//     */
//    JavaCompiler getCompiler();
}
