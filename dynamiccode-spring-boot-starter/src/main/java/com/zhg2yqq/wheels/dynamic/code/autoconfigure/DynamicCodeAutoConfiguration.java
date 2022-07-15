package com.zhg2yqq.wheels.dynamic.code.autoconfigure;

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
import com.zhg2yqq.wheels.dynamic.code.autoconfigure.DynamicCodeProperties.RunClassProperties;
import com.zhg2yqq.wheels.dynamic.code.autoconfigure.DynamicCodeProperties.RunSourceProperties;
import com.zhg2yqq.wheels.dynamic.code.core.ClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.core.StringJavaCompiler;

@Configuration
@EnableConfigurationProperties(DynamicCodeProperties.class)
public class DynamicCodeAutoConfiguration {
    private final DynamicCodeProperties properties;

    public DynamicCodeAutoConfiguration(DynamicCodeProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public IStringCompiler stringCompiler() {
        return new StringJavaCompiler();
    }

    @Bean
    @ConditionalOnMissingBean
    public IClassExecuter classExecuter() {
        return new ClassExecuter();
    }

    @Bean
    @ConditionalOnMissingBean
    public RunClassHandler runClassHandler(IStringCompiler compiler, IClassExecuter executer) {
        RunClassProperties classProperties = properties.getClassHandler();
        return new RunClassHandler(compiler, executer, classProperties, hacker());
    }

    @Bean
    @ConditionalOnMissingBean
    public RunSourceHandler runSourceHandler(IStringCompiler compiler, IClassExecuter executer) {
        RunSourceProperties sourceProperties = properties.getSourceHandler();
        return new RunSourceHandler(compiler, executer, sourceProperties, hacker(),
                sourceProperties.getCacheSize());
    }

    public Map<String, String> hacker() {
        if (properties.getHacker() == null) {
            Map<String, String> hackers = new HashMap<>();
            hackers.put("java/io/File", "com/zhg2yqq/wheels/dynamic/code/hack/HackFile");
            return hackers;
        } else {
            return properties.getHacker();
        }
    }
}
