/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.autoconfigure;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zhg2yqq.wheels.dynamic.code.IClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.IStringCompiler;
import com.zhg2yqq.wheels.dynamic.code.RunClassHandler;
import com.zhg2yqq.wheels.dynamic.code.RunSourceHandler;
import com.zhg2yqq.wheels.dynamic.code.config.RunClassProperties;
import com.zhg2yqq.wheels.dynamic.code.config.RunSourceProperties;
import com.zhg2yqq.wheels.dynamic.code.core.ClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.core.JaninoCompiler;
import com.zhg2yqq.wheels.dynamic.code.core.StringJavaCompiler;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.factory.AbstractCompilerFactory;
import com.zhg2yqq.wheels.dynamic.code.factory.StandardCompilerFactory;

/**
 * 自动配置
 *
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月18日
 */
@Configuration
@EnableConfigurationProperties(DynamicCodeProperties.class)
public class DynamicCodeAutoConfiguration {
    private final DynamicCodeProperties properties;

    public DynamicCodeAutoConfiguration(DynamicCodeProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public IStringCompiler stringCompiler() throws Exception {
        if (properties.getJdkToolUrl() != null && !properties.getJdkToolUrl().isEmpty()) {
            URL jdkToolUrl = new URL(properties.getJdkToolUrl());
            AbstractCompilerFactory compilerFactory = new StandardCompilerFactory(jdkToolUrl);
            return new StringJavaCompiler(compilerFactory);
        } else {
            return new JaninoCompiler();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public IClassExecuter<ExecuteResult> classExecuter() {
        return new ClassExecuter();
    }

    @Bean
    @ConditionalOnMissingBean
    public RunClassHandler runClassHandler(IStringCompiler compiler,
                                                          IClassExecuter<ExecuteResult> executer) {
        RunClassProperties classProperties = properties.getClassHandler();
        return new RunClassHandler(compiler, executer, classProperties, hacker());
    }

    @Bean
    @ConditionalOnMissingBean
    public RunSourceHandler runSourceHandler(IStringCompiler compiler,
                                                            IClassExecuter<ExecuteResult> executer) {
        RunSourceProperties sourceProperties = properties.getSourceHandler();
        return new RunSourceHandler(compiler, executer, sourceProperties, hacker(),
                sourceProperties.getCacheSize());
    }

    public Map<String, String> hacker() {
        if (properties.getHacker() == null) {
            Map<String, String> hackers = new HashMap<>(1);
            hackers.put("java/io/File", "com/zhg2yqq/wheels/dynamic/code/hack/HackFile");
            return hackers;
        } else {
            return properties.getHacker();
        }
    }
}
