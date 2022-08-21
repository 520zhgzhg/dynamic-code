/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.core;

import java.util.Map;

import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.ICompilerFactory;
import org.codehaus.commons.compiler.ISimpleCompiler;

import com.zhg2yqq.wheels.dynamic.code.IStringCompiler;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年8月17日
 */
public class JaninoCompiler implements IStringCompiler {
    private final ICompilerFactory factory;
    public JaninoCompiler() throws Exception {
        this(CompilerFactoryFactory.getDefaultCompilerFactory());
    }
    
    public JaninoCompiler(ICompilerFactory factory) {
        this.factory = factory;
    }

    /**
     * @param fullClassName
     * @param sourceCode
     * @param calTime
     * @return
     * @throws CompileException
     * @see com.zhg2yqq.wheels.dynamic.code.IStringCompiler#compile(java.lang.String, java.lang.String, com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO)
     */
    @Override
    public CompileResult compile(String fullClassName, String sourceCode, CalTimeDTO calTime)
        throws CompileException {
        long compilerTakeTime = -1;
        byte[] compiledBytes = null;
        CompileResult result = new CompileResult(fullClassName);
        if (calTime.isCalCompileTime()) {
            long startTime = System.currentTimeMillis();
            // 编译成字节，并将字节对象放入result
            compiledBytes = compile(fullClassName, sourceCode);
            // 设置编译耗时(单位ms)
            compilerTakeTime = System.currentTimeMillis() - startTime;
        } else {
            // 编译成字节，并将字节对象放入result
            compiledBytes = compile(fullClassName, sourceCode);
        }
        result.setCompiledBytes(compiledBytes);
        result.setCompileTime(compilerTakeTime);
        return result;
    }

    private byte[] compile(String fullClassName, String sourceCode) throws CompileException {
        ISimpleCompiler compiler = factory.newSimpleCompiler();
        try {
            compiler.cook(sourceCode);
        } catch (org.codehaus.commons.compiler.CompileException e) {
            throw new CompileException(e.getMessage(), e);
        }
        Map<String, byte[]> bytes = compiler.getBytecodes();
        return bytes.get(fullClassName);
    }
}
