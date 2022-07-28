/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.compiler.analysis;

import com.zhg2yqq.wheels.compiler.exception.TokenException;

/**
 * 状态机转换
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月28日
 */
public interface IStateMachineStrategy {
    State getSelfState();
    
    State transform(char c) throws TokenException;
}
