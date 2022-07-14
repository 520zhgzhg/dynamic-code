/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.CompileResult;

/**
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public interface IStringCompiler {
    CompileResult compiler(String fullClassName, String sourceCode, CalTimeDTO calTime);
}
