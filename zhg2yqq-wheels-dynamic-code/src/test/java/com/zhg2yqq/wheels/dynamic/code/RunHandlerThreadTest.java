/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.zhg2yqq.wheels.dynamic.code.core.ClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.core.StringJavaCompiler;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.exception.BaseDynamicException;

/**
 * 执行Java代码测试
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class RunHandlerThreadTest {
    private Logger log = Logger.getLogger(RunHandlerThreadTest.class.getName());
    /**
     * 预编译单线程测试
     * @throws BaseDynamicException 
     */
    @Test
    public void testRunClassSingle() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        CalTimeDTO calTime = new CalTimeDTO();
        calTime.setCalExecuteTime(true);
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();

        System.out.println("预编译单线程测试 start");
        RunClassHandler handler = new RunClassHandler(compiler, executer, calTime, hackers);
        
        // 待预编译源码
        List<String> preloadSources = new ArrayList<>();
        preloadSources.add("package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    private int sum = 0;\n"
                + "    public int add(int num) {\n"
                + "        sum += num;\n"
                + "        return sum;\n" 
                + "    }\n" 
                + "}");
        // 预编译
        handler.loadClassFromSources(preloadSources);

        int times = 10000;
        // 执行类
        Parameters pars = new Parameters();
        pars.add(1);
        // 非单例测试
        int costTime = 0;
        for (int i = 0; i < times; i++) {
            ExecuteResult result = handler.runMethod("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "add",
                    pars);
            costTime += result.getRunTakeTime();
            Assert.assertEquals(1, result.getReturnVal());
        }
        System.out.println("非单例执行时间：" + costTime);

        // 单例测试
        costTime = 0;
        for (int i = 0; i < times; i++) {
            ExecuteResult result = handler.runMethod("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "add",
                    pars, true);
            costTime += result.getRunTakeTime();
            Assert.assertEquals(1 + i, result.getReturnVal());
        }
        System.out.println("单例执行时间：" + costTime);

        System.out.println("预编译单线程测试 end");
    }
    
    /**
     * 预编译多线程测试
     * @throws BaseDynamicException 
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    @Test
    public void testRunClassMulti() throws BaseDynamicException, InterruptedException, ExecutionException {
        Map<String, String> hackers = new HashMap<>();
        CalTimeDTO calTime = new CalTimeDTO();
        calTime.setCalExecuteTime(true);
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();

        System.out.println("预编译多线程测试 start");
        RunClassHandler handler = new RunClassHandler(compiler, executer, calTime, hackers);
        
        // 待预编译源码
        List<String> preloadSources = new ArrayList<>();
        preloadSources.add("package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    public int add(int num0, int num1) {\n"
                + "        return num0 + num1;\n"
                + "    }\n" 
                + "}");
        // 预编译
        handler.loadClassFromSources(preloadSources);

        int times = 10000;
        List<Future<Object>> results = new ArrayList<>(times);
        ExecutorService es = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        // 执行类
        Parameters pars = new Parameters();
        pars.add(1);
        pars.add(99);
        // 非单例测试
        final AtomicLong costTime0 = new AtomicLong(0);
        for (int i = 0; i < times; i++) {
            results.add(es.submit(() -> {
                try {
                    ExecuteResult result = handler.runMethod("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "add",
                            pars);
                    costTime0.addAndGet(result.getRunTakeTime());
                    return result.getReturnVal();
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                    return -1;
                }
            }));
        }
        for(Future<Object> future : results) {
            Assert.assertEquals(100, future.get());
        }
        System.out.println("非单例执行时间：" + costTime0.get());

        // 单例测试
        results.clear();
        final AtomicLong costTime1 = new AtomicLong(0);
        for (int i = 0; i < times; i++) {
            results.add(es.submit(() -> {
                try {
                    ExecuteResult result = handler.runMethod("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "add",
                            pars, true);
                    costTime1.addAndGet(result.getRunTakeTime());
                    return result.getReturnVal();
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                    return -1;
                }
            }));
        }
        for(Future<Object> future : results) {
            Assert.assertEquals(100, future.get());
        }
        System.out.println("单例执行时间：" + costTime1.get());

        es.shutdown();
        System.out.println("预编译多线程测试 end");
    }

    /**
     * 源码单线程测试
     * @throws BaseDynamicException 
     */
    @Test
    public void testRunSourceSingle() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        CalTimeDTO calTime = new CalTimeDTO();
        calTime.setCalExecuteTime(true);
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();

        System.out.println("源码单线程测试 start");
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);

        int times = 10000;
        String source = "package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    private int sum = 0;\n"
                + "    public int add(int num) {\n"
                + "        sum += num;\n"
                + "        return sum;\n" 
                + "    }\n" 
                + "}";
        // 执行源码
        Parameters args = new Parameters();
        args.add(1);
        // 非单例测试
        int costTime = 0;
        for (int i = 0; i < times; i++) {
            ExecuteResult result = handler.runMethod(source, "add", args);
            costTime += result.getRunTakeTime();
            Assert.assertEquals(1, result.getReturnVal());
        }
        System.out.println("非单例执行时间：" + costTime);

        // 单例测试
        costTime = 0;
        for (int i = 0; i < times; i++) {
            ExecuteResult result = handler.runMethod(source, "add", args, true);
            costTime += result.getRunTakeTime();
            Assert.assertEquals(1 + i, result.getReturnVal());
        }
        System.out.println("单例执行时间：" + costTime);
        
        // 重新编译
        costTime = 0;
        handler.loadClassFromSource("package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    private int sum = 0;\n"
                + "    public int add(int num) {\n"
                + "        sum += (num << 1);\n"
                + "        return sum;\n" 
                + "    }\n" 
                + "}");
        for (int i = 0; i < times; i++) {
            ExecuteResult result = handler.runMethod(source, "add", args, true);
            costTime += result.getRunTakeTime();
            Assert.assertEquals(2 * (i + 1), result.getReturnVal());
        }
        System.out.println("重新编译执行时间：" + costTime);

        System.out.println("源码单线程测试 end");
    }
    
    /**
     * 源码多线程测试
     * @throws BaseDynamicException 
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    @Test
    public void testRunSourceMulti() throws BaseDynamicException, InterruptedException, ExecutionException {
        Map<String, String> hackers = new HashMap<>();
        CalTimeDTO calTime = new CalTimeDTO();
        calTime.setCalExecuteTime(true);
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();

        System.out.println("源码多线程测试 start");
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);
        
        // 待预编译源码
        String source = "package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    public int add(int num0, int num1) {\n"
                + "        return num0 + num1;\n"
                + "    }\n" 
                + "}";

        int times = 10000;
        List<Future<Object>> results = new ArrayList<>(times);
        ExecutorService es = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        // 执行类
        Parameters pars = new Parameters();
        pars.add(1);
        pars.add(99);
        // 非单例测试
        final AtomicLong costTime0 = new AtomicLong(0);
        for (int i = 0; i < times; i++) {
            results.add(es.submit(() -> {
                try {
                    ExecuteResult result = handler.runMethod(source, "add", pars);
                    costTime0.addAndGet(result.getRunTakeTime());
                    return result.getReturnVal();
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                    return -1;
                }
            }));
        }
        for(Future<Object> future : results) {
            Assert.assertEquals(100, future.get());
        }
        System.out.println("非单例执行时间：" + costTime0.get());

        // 单例测试
        results.clear();
        final AtomicLong costTime1 = new AtomicLong(0);
        for (int i = 0; i < times; i++) {
            results.add(es.submit(() -> {
                try {
                    ExecuteResult result = handler.runMethod(source, "add", pars, true);
                    costTime1.addAndGet(result.getRunTakeTime());
                    return result.getReturnVal();
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage(), e);
                    return -1;
                }
            }));
        }
        for(Future<Object> future : results) {
            Assert.assertEquals(100, future.get());
        }
        System.out.println("单例执行时间：" + costTime1.get());

        es.shutdown();
        System.out.println("源码多线程测试 end");
    }
}