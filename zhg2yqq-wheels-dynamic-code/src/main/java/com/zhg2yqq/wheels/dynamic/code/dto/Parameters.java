/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 非线程安全，class待调用方法参数（特别注意需要区分包装类型与基本数据类型）
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月11日
 */
public class Parameters {
    private List<Parameter> params = new ArrayList<>();

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序（特别注意需要区分包装类型与基本数据类型）
     * 
     * @param arg 参数，可为null
     * @param argClazz 参数类型，当参数arg不为null时，该参数可为null；否则不能为null
     */
    public void add(Object arg, Class<Object> argClazz) {
        Parameter param = new Parameter(arg == null ? argClazz : arg.getClass(), arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序（特别注意需要区分包装类型与基本数据类型）
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

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg byte基础类型
     */
    public void add(byte arg) {
        Parameter param = new Parameter(byte.class, arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg short基础类型
     */
    public void add(short arg) {
        Parameter param = new Parameter(short.class, arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg int基础类型
     */
    public void add(int arg) {
        Parameter param = new Parameter(int.class, arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg long基础类型
     */
    public void add(long arg) {
        Parameter param = new Parameter(long.class, arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg float基础类型
     */
    public void add(float arg) {
        Parameter param = new Parameter(float.class, arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg double基础类型
     */
    public void add(double arg) {
        Parameter param = new Parameter(double.class, arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg char基础类型
     */
    public void add(char arg) {
        Parameter param = new Parameter(char.class, arg);
        params.add(param);
    }

    /**
     * 调用的方法参数，注意先后顺序，必须符合方法参数的顺序
     * 
     * @param arg boolean基础类型
     */
    public void add(boolean arg) {
        Parameter param = new Parameter(boolean.class, arg);
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
