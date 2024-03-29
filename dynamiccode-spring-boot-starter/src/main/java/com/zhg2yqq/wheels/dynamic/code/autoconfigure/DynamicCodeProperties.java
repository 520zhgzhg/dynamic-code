/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.autoconfigure;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * 动态代码配置
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月13日
 */
@Getter @Setter
@ConfigurationProperties(prefix = "dynamic.code")
public class DynamicCodeProperties {
    /**
     * 自定义编译jar包工具加载URL路径，为空时默认使用工具包自带编译工具（版本为1.8.0_201，LITE版本不能为空）
     * 示例：file:/C:/Program Files/Java/jdk1.8.0_201/lib/tools.jar
     */
    private String jdkToolUrl;
    /**
     * 替换代码中风险类
     * （key:待替换的类名,例如:java/io/File，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackFile）
     * （也可key:待替换的类名,例如:java.io.File，value:替换成的类名,例如:com.zhg2yqq.wheels.dynamic.code.hack.HackFile）
     */
    private Map<String, String> hacker;
    /**
     * 源码执行器配置
     */
    private RunSourceProperties sourceHandler = new RunSourceProperties();
    /**
     * Class执行器配置
     */
    private RunClassProperties classHandler = new RunClassProperties();

    @Getter @Setter
    public static class RunSourceProperties extends CalTimeDTO {
        /**
         * 源码执行器的缓存容器大小
         */
        private int cacheSize = 100;
    }

    @Getter @Setter
    public static class RunClassProperties extends CalTimeDTO {
    }
}
