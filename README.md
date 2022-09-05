# 执行动态代码

## 一、介绍
基于Java8 在运行时动态编译源码文本字符串，实现动态运行代码。可实现在线运行Java代码、动态策略、动态配置等相关业务场景需求。支持缓存单例提升方法执行效率，支持设置方法超时时间避免死循环等耗时方法长时间占用资源。

## 二、软件架构
#### 1. 基础 zhg2yqq-wheels-dynamic-code
![avatar](http://resource.zhg2yqq.com/image/zhg2yqq-wheels-dynamic-code%E7%BB%93%E6%9E%84%E5%9B%BE.png)
#### 2. dynamiccode-spring-boot-starter
包含相关SpringBoot自动配置。


## 三、流程
动态执行代码方法大致流程如下
```mermaid
graph LR
S[源码]-->|源码文本| C[编译源码]-->|字节码| L(加载类)-->|Class类| E(调用类方法)-->R[方法执行结果]
```


## 四、使用说明


> 1. 基于zhg2yqq-wheels-dynamic-code
>> pom中引入jar包
>> ```
>>     <dependency>
>>         <groupId>com.zhg2yqq</groupId>
>>         <artifactId>wheels-dynamic-code</artifactId>
>>         <version>1.8.3</version>
>>     </dependency>
>> ```
>> 如果只需要编译一次（源码不会变的情况），建议实例化全局唯一的RunClassHandler对象。
>> ```
>>     // 根据实际需求创建执行源码方法类,先创建以下构造实例必要参数。
>>     // 安全替换（key:待替换的类名,例如:java/io/File，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackFile）
>>     Map<String, String> hackers = new HashMap<>();
>>     // 是否需要返回编译、调用源码方法运行用时
>>     RunClassProperties calTime = new RunClassProperties();
>>     // 编译器
>>     IStringCompiler compiler = new StringJavaCompiler();
>>     // 执行器
>>     IClassExecuter<ExecuteResult> executer = new ClassExecuter();
>>
>>     RunClassHandler handler = new RunClassHandler(compiler, executer, calTime, hackers);
>>     // 预编译加载源码类
>>     handler.loadClassFromSources("Java源代码1", "Java源代码2");
>> 
>>     // 执行指定类方法
>>     // 严格按顺序构造待调用方法的入参参数
>>     Parameters args = new Parameters();
>>     String[] pars = new String[0];
>>     args.add(pars);
>>     // 传入参数调用类指定方法获取执行结果
>>     ExecuteResult result = handler.runMethod("com.zhg2yqq.bill.Test", "main", args);
>>     ...
>> 
>>     // 当然，如果确实需要重新编译覆盖类，也可重新编译
>>     // handler.loadClassFromSource("Java源代码1");
>>     // 支持单例，相同类只会实例化一个对象，避免重复创建相同对象影响效率
>>     handler.runMethod("com.zhg2yqq.bill.Test", "main", args, true);
>> ```
>> 如果源码可能变化需要重新编译，可实例化RunSourceHandler对象。
>> ```
>>     // 根据实际需求创建执行源码方法类,先创建以下构造实例必要参数。
>>     // 安全替换（key:待替换的类名,例如:java/io/File，value:替换成的类名,例如:com/zhg2yqq/wheels/dynamic/code/hack/HackFile）
>>     Map<String, String> hackers = new HashMap<>();
>>     // 是否需要返回编译、调用源码方法运行用时
>>     RunSourceProperties calTime = new RunSourceProperties();
>>     // 编译器
>>     IStringCompiler compiler = new StringJavaCompiler();
>>     // 执行器
>>     IClassExecuter<ExecuteResult> executer = new ClassExecuter();
>>
>>     RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);
>> 
>>     // 执行指定类方法
>>     // 严格按顺序构造待调用方法的入参参数
>>     Parameters args = new Parameters();
>>     args.add("  zhg2yqq  测试2");
>>     args.add("zhg2yqq");
>>     // 传入参数调用类指定方法获取执行结果
>>     ExecuteResult result = handler.runMethod("Java源代码", "replaceStr", args);
>> 
>>     // 存在多次调用相同源码相同类时，可使用单例方式执行方法，有效提高效率
>>     ExecuteResult result = handler.runMethod("Java源代码", "replaceStr", args, true);
>> 
>>     // 若需重新编译相同类（同package、同class名），新的class类将会覆盖原旧类
>>     ExecuteResult result = handler.runMethod("Java源代码", "replaceStr", args, false, true);
>>     // 以下也可运行重新编译后的类方法
>>     // handler.loadClassFromSource("Java源代码");
>>     // ExecuteResult result = handler.runMethod("Java源代码", "replaceStr", args)
>>     ...
>> ```
>> RunXXXHandler使用方法可参考单元测试类RunHandlerTest。
>>
> 2. 基于dynamiccode-spring-boot-starter
>> pom中引入jar包
>> ```
>>     <dependency>
>>         <groupId>com.zhg2yqq</groupId>
>>         <artifactId>dynamiccode-spring-boot-starter</artifactId>
>>         <version>1.8.3</version>
>>     </dependency>
>> ```
>> application.properties可配置项
>> ```
>> # 自定义编译jdk工具加载URL路径，为空时默认使用Janino编译工具
>> #dynamic.code.jdk-tool-url=file:/C:/Program Files/Java/jdk1.8.0_201/lib/tools.jar
>> # 替换代码中风险类（key:待替换的类名,例如:java.io.File，value:替换成的类名,例如:com.zhg2yqq.wheels.dynamic.code.hack.HackFile）
>> dynamic.code.hacker.java.io.File=com.zhg2yqq.wheels.dynamic.code.hack.HackFile
>> # RunSourceHandler配置
>> # Class缓存大小，默认100
>> dynamic.code.source-handler.cache-size=100
>> # 是否统计编译耗时，默认false
>> dynamic.code.source-handler.cal-compile-time=false
>> # 是否统计源码方法执行耗时，默认false
>> dynamic.code.source-handler.cal-execute-time=false
>> # 是否支持重复加载相同类，默认true，新编译加载的类替换缓存的原始类，类与类之间的加载器不同；false，新编译的相同类重复加载时将报错，类与类之间的加载器相同
>> #dynamic.code.source-handler.support-reload=true
>> # 方法执行超时时间，单位：毫秒（防止死循环占用，小于等于0时表示无限制，默认0）
>> dynamic.code.source-handler.execute-time-out=10000
>> # RunClassHandler配置
>> # 是否统计编译耗时，默认false
>> dynamic.code.class-handler.cal-compile-time=false
>> # 是否统计源码方法执行耗时，默认false
>> dynamic.code.class-handler.cal-execute-time=false
>> # 是否支持重复加载相同类，默认true，新编译加载的类替换缓存的原始类，类与类之间的加载器不同；false，新编译的相同类重复加载时将报错，类与类之间的加载器相同
>> #dynamic.code.class-handler.support-reload=false
>> # 方法执行超时时间，单位：毫秒（防止死循环占用，小于等于0时表示无限制，默认0）
>> dynamic.code.class-handler.execute-time-out=10000
>> ```
>> 可根据实际需要注入Handler类
>> ```
>>     @Autowired
>>     private RunClassHandler runClassHandler;
>>     @Autowired
>>     private RunSourceHandler runSourceHandler;
>> ```


### 使用征集
如果可以，请使用zhg2yqq-dynamic-code的公司请在 [https://gitee.com/youle/zhg2yqq-dynamic-code/issues/I5HCCV](https://gitee.com/youle/zhg2yqq-dynamic-code/issues/I5HCCV) 留下 ”公司名称 + 公司官网地址“ ，感谢支持。

## 五、参与贡献

目前只有一个人辛苦，欢迎大家Star、Fork、PR。


## 六、期望
个人能力有限，欢迎提出更好的意见，帮助完善。
