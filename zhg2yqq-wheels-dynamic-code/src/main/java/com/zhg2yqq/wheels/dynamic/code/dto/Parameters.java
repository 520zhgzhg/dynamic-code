/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 非线程安全
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class Parameters {
    private List<Parameter> params = new ArrayList<>();

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg 参数，可为null
     * @param argClazz 参数类型，当参数arg不为null时，该参数可为null；否则不能为null
     */
    public void add(Object arg, Class<Object> argClazz) {
        Parameter param = new Parameter(arg == null ? argClazz : arg.getClass(), arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg 参数，不能为null
     */
    public void add(Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("参数为null，无法获取类型");
        }
        Parameter param = new Parameter(arg.getClass(), arg);
        params.add(param);
    }

    public List<Parameter> getParameters() {
        return params;
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }
    
    public void clear() {
        params.clear();
    }
    
    /**
     * 方法各个参数
     */
    public static class Parameter {
        private Class<?> clazz;
        private Object value;

        private Parameter(Class<?> clazz, Object value) {
            this.clazz = clazz;
            this.value = value;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public Object getVaule() {
            return value;
        }
    }
}
