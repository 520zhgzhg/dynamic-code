/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters.Parameter;

/**
 * 类工具，包含根据源码获取类全名、执行类方法
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class ClassUtils {
    /**
     * package名正则表达式
     */
    private static final Pattern PACKAGE_NAME_PATTERN = Pattern.compile("package\\s+\\S+\\s*;");
    /**
     * class名正则表达式
     */
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("class\\s+\\S+\\s+\\{");

    /**
     * 调用类中方法
     * 
     * @param clazz 类
     * @param methodName 方法名
     * @param args 方法参数
     * @return 方法执行结果
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object runClassMethod(Class<?> clazz, String methodName, Parameters args)
        throws NoSuchMethodException, InstantiationException, IllegalAccessException,
        InvocationTargetException {
        Object[] values = null;
        Object target = null;
        Method method;
        if (args == null || args.isEmpty()) {
            // 如果无参，找类中无参方法
            method = clazz.getMethod(methodName);
        } else {
            // 如果有参，找类中参数顺序相符的方法
            List<Parameter> params = args.getParameters();
            Class<?>[] clazzes = new Class[params.size()];
            values = new Object[params.size()];
            for (int i = 0; i < clazzes.length; i++) {
                Parameter param = params.get(i);
                clazzes[i] = param.getClazz();
                values[i] = param.getVaule();
            }
            method = clazz.getMethod(methodName, clazzes);
        }

        if (!Modifier.isStatic(method.getModifiers())) {
            // 如果方法是不是静态方法，则需要先实例类
            target = getClassInstance(clazz);
        }
        return method.invoke(target, values);
    }

    /**
     * 根据class获取实例
     * 
     * @param <T> 实例类型
     * @param clazz .
     * @return 实例
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T getClassInstance(Class<T> clazz) throws NoSuchMethodException,
        InvocationTargetException, InstantiationException, IllegalAccessException {
        // 源码必须含有可访问的无参构造方法
        Constructor<T> noArgConstructor = clazz.getConstructor();
        return noArgConstructor.newInstance();
    }

    /**
     * 调用实例中方法
     * 
     * @param instance 实例
     * @param methodName 方法名
     * @param args 方法参数
     * @return 方法执行结果
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object runInstanceMethod(Object instance, String methodName, Parameters args)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (instance == null) {
            throw new IllegalArgumentException("实例不能为空");
        }
        Class<?> clazz = instance.getClass();
        Object[] values = null;
        Method method;
        if (args == null || args.isEmpty()) {
            // 如果无参，找类中无参方法
            method = clazz.getMethod(methodName);
        } else {
            // 如果有参，找类中参数顺序相符的方法
            List<Parameter> params = args.getParameters();
            Class<?>[] clazzes = new Class[params.size()];
            values = new Object[params.size()];
            for (int i = 0; i < clazzes.length; i++) {
                Parameter param = params.get(i);
                clazzes[i] = param.getClazz();
                values[i] = param.getVaule();
            }
            method = clazz.getMethod(methodName, clazzes);
        }
        return method.invoke(instance, values);
    }

    /**
     * 获取类的全名称
     *
     * @param sourceCode 源码
     * @return 类的全名称
     */
    public static String getFullClassName(String sourceCode) {
        String className = "";
        Matcher matcher = PACKAGE_NAME_PATTERN.matcher(sourceCode);
        if (matcher.find()) {
            className = matcher.group().replaceFirst("package", "").replace(";", "").trim() + ".";
        }

        matcher = CLASS_NAME_PATTERN.matcher(sourceCode);
        if (matcher.find()) {
            className += matcher.group().replaceFirst("class", "").replace("{", "").trim();
        }
        return className;
    }
}
