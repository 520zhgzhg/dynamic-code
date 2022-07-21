# 执行动态代码

## 介绍
基于Java8 在运行时动态编译源码文本字符串，实现动态运行代码。可实现在线运行Java代码、动态策略、动态配置等相关业务场景需求。

## 软件架构
#### 1. 基础 zhg2yqq-wheels-dynamic-code
![avatar](http://resource.zhg2yqq.com/image/zhg2yqq-wheels-dynamic-code%E7%BB%93%E6%9E%84%E5%9B%BE.png)
#### 2. dynamiccode-spring-boot-starter
包含相关SpringBoot自动配置。


## 流程
动态执行代码方法大致流程如下
```mermaid
graph LR
S[源码]-->|源码文本| C[编译源码]-->|字节码| L(加载类)-->|Class类| E(调用类方法)-->R[方法执行结果]
```


## 使用说明

#### 1. 基于zhg2yqq-wheels-dynamic-code
pom中引入jar包
```
    <dependency>
        <groupId>com.zhg2yqq</groupId>
        <artifactId>wheels-dynamic-code</artifactId>
        <version>1.8.1</version>
    </dependency>
```
根据实际需求创建执行源码方法类,先创建以下构造实例必要参数。
```
    // 安全替换（key:待替换的类名,例如:java/io/File，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackFile）
    Map<String, String> hackers = new HashMap<>();
    // 是否需要返回编译、调用源码方法运行用时
    CalTimeDTO calTime = new CalTimeDTO();
    // 编译器
    IStringCompiler compiler = new StringJavaCompiler();
    // 执行器
    IClassExecuter executer = new ClassExecuter();
```
如果只需要编译一次（源码不会变的情况），建议实例化全局唯一的RunClassHandler对象。
```
    RunClassHandler handler = new RunClassHandler(compiler, executer, calTime, hackers);
    // 预编译加载源码类
    handler.preloadClass(...);

    // 执行指定类方法
    // 严格按顺序构造待调用方法的入参参数
    Parameters args = new Parameters();
    String[] pars = new String[0];
    args.add(pars);
    // 传入参数调用类指定方法获取执行结果
    ExecuteResult result = handler.runClassJava("com.zhg2yqq.bill.Test", "main", args);
    ...
```
如果源码可能变化需要重新编译，可实例化RunSourceHandler对象。
```
    RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);

    // 执行指定类方法
    // 严格按顺序构造待调用方法的入参参数
    Parameters args = new Parameters();
    args.add("  zhg2yqq  测试2");
    args.add("zhg2yqq");
    // 传入参数调用类指定方法获取执行结果
    ExecuteResult result = handler.runSourceJava("Java源代码", "replaceStr", args);
    ...

    // 若需重新编译相同类（同package、同class名），新的class类将会覆盖原旧类
    ExecuteResult result = handler.runSourceJava("Java源代码", "replaceStr", args, true);
    ...
```
RunXXXHandler使用方法可参考单元测试类RunHandlerTest。
#### 2. 基于dynamiccode-spring-boot-starter
pom中引入jar包
```
    <dependency>
        <groupId>com.zhg2yqq</groupId>
        <artifactId>dynamiccode-spring-boot-starter</artifactId>
        <version>1.8.1</version>
    </dependency>
```
application.properties可配置项
```
# 替换代码中风险类（key:待替换的类名,例如:java.io.File，value:替换成的类名,例如:com.zhg2yqq.wheels.dynamic.code.hack.HackFile）
dynamic.code.hacker.java.io.File=com.zhg2yqq.wheels.dynamic.code.hack.HackFile
#dynamic.code.hacker.
# RunSourceHandler配置
# Class缓存大小（LRU原则删除加载过的Class），默认100
dynamic.code.source-handler.cache-size=100
# 是否统计编译耗时，默认false
dynamic.code.source-handler.cal-compile-time=false
# 是否统计源码方法执行耗时，默认false
dynamic.code.source-handler.cal-execute-time=false
# RunClassHandler配置
# 是否统计编译耗时，默认false
dynamic.code.class-handler.cal-compile-time=false
# 是否统计源码方法执行耗时，默认false
dynamic.code.class-handler.cal-execute-time=false
```
可根据实际需要注入Handler类
```
    @Autowired
    private RunClassHandler runClassHandler;
    @Autowired
    private RunSourceHandler runSourceHandler;
```
### 使用征集
如果可以，请使用zhg2yqq-dynamic-code的公司请在 [https://github.com/520zhgzhg/dynamic-code/issues/2) 留下 ”公司名称 + 公司官网地址“ ，感谢支持。

## 参与贡献

目前只有一个人辛苦，欢迎大家Star、Fork、PR。


## 期望
个人能力有限，欢迎提出更好的意见，帮助完善。
