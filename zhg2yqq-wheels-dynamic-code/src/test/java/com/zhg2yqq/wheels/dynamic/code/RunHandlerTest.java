/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.zhg2yqq.wheels.dynamic.code.core.ClassExecuter;
import com.zhg2yqq.wheels.dynamic.code.core.StringJavaCompiler;
import com.zhg2yqq.wheels.dynamic.code.dto.CalTimeDTO;
import com.zhg2yqq.wheels.dynamic.code.dto.ExecuteResult;
import com.zhg2yqq.wheels.dynamic.code.dto.Parameters;
import com.zhg2yqq.wheels.dynamic.code.exception.BaseDynamicException;
import com.zhg2yqq.wheels.dynamic.code.exception.CompileException;
import com.zhg2yqq.wheels.dynamic.code.exception.ExecuteException;

/**
 * 执行Java代码测试
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月8日
 */
public class RunHandlerTest {
    /**
     * 预编译测试
     * @throws BaseDynamicException 
     */
    @Test
    public void testRunClass() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        CalTimeDTO calTime = new CalTimeDTO();
        calTime.setCalExecuteTime(true);
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();
        RunClassHandler handler = new RunClassHandler(compiler, executer, calTime, hackers);

        System.out.println("预编译测试 start");
        
        // 待预编译源码
        List<String> preloadSources = new ArrayList<>();
        preloadSources.add("/*\n" 
                + " * Copyright (c) zhg2yqq Corp.\n"
                + " * All Rights Reserved.\n" 
                + " */\n" 
                + "package com.zhg2yqq.bill;\n"
                + "/**\n" 
                + " * @version zhg2yqq v1.0\n" 
                + " * @author 周海刚, 2022年6月8日\n"
                + " */\n" 
                + "public class Test {\n" 
                + "    /**\n" 
                + "     * 无聊时的精\n"
                + "     * @param args\n" 
                + "     */\n"
                + "    public static void main(String[] args) {\n" 
                + "        // 测试打印\n"
                + "        System.out.println(\"normal\");\n"
                + "        System.err.println(\"error\");\n" 
                + "    }\n" 
                + "}");
        preloadSources.add("package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    public String trimStr(String str) {\n"
                + "        return str.trim();\n" 
                + "    }\n" 
                + "}");
        // 预编译
        handler.loadClassFromSources(preloadSources);

        // 执行类
        Parameters args0 = new Parameters();
        String[] pars0 = new String[0];
        args0.add(pars0);
        ExecuteResult result0 = handler.runMethod("com.zhg2yqq.bill.Test", "main", args0);
        Assert.assertNull(result0.getReturnVal());

        Parameters pars1 = new Parameters();
        pars1.add("    测试1     ");
        ExecuteResult result1 = handler.runMethod("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "trimStr",
                pars1);
        Assert.assertEquals("测试1", result1.getReturnVal());

        Parameters pars2 = new Parameters();
        pars2.add("  zhg2yqq  测试2");
        ExecuteResult result2 = handler.runMethod("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "trimStr",
                pars2);
        Assert.assertEquals("zhg2yqq  测试2", result2.getReturnVal());

        System.out.println("预编译测试 end");
    }

    /**
     * 源码测试
     * @throws BaseDynamicException 
     */
    @Test
    public void testRunSource() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        CalTimeDTO calTime = new CalTimeDTO();
        calTime.setCalCompileTime(true);
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);

        System.out.println("源码测试 start");

        // 执行源码
        Parameters args0 = new Parameters();
        String[] pars0 = new String[0];
        args0.add(pars0);
        ExecuteResult result0 = handler.runMethod("/*\n" 
                + " * Copyright (c) zhg2yqq Corp.\n" 
                + " * All Rights Reserved.\n"
                + " */\n" 
                + "package com.zhg2yqq.bill;\n" 
                + "/**\n"
                + " * @version zhg2yqq v1.0\n" 
                + " * @author 周海刚, 2022年6月8日\n"
                + " */\n" 
                + "public class Test {\n" 
                + "    /**\n" 
                + "     * 无聊时的精\n"
                + "     * @param args\n" 
                + "     */\n"
                + "    public static void main(String[] args) {\n" 
                + "        // 测试打印\n"
                + "        System.out.println(\"normal\");\n"
                + "        System.err.println(\"error\");\n" 
                + "    }\n" 
                + "}",
                "main", args0);
        Assert.assertNull(result0.getReturnVal());

        Parameters pars1 = new Parameters();
        pars1.add("    测试1     ");
        ExecuteResult result1 = handler.runMethod("package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    public String trimStr(String str) {\n"
                + "        return str.trim();\n" 
                + "    }\n" 
                + "}", "trimStr",
                pars1);
        Assert.assertEquals("测试1", result1.getReturnVal());

        // 重新编译加载源码类
        Parameters pars2 = new Parameters();
        pars2.add("    测试2     ");
        ExecuteResult result2 = handler.runMethod(
                "package com.zhg2yqq.wheels.dynamic.code;\n" 
                + "public class CodeTemplate {\n"
                + "    public String trimStr(String str) {\n"
                + "        return str + \"zhg2yqq\";\n" 
                + "    }\n" 
                + "}",
                "trimStr", pars2, false, true);
        Assert.assertEquals("    测试2     zhg2yqq", result2.getReturnVal());

        result1 = handler.runMethod("package com.zhg2yqq.wheels.dynamic.code;\n"
                + "public class CodeTemplate {\n"
                + "    public String trimStr(String str) {\n"
                + "        return str.trim();\n" 
                + "    }\n" 
                + "}", "trimStr", pars1);
        Assert.assertEquals("    测试1     zhg2yqq", result1.getReturnVal());

        System.out.println("源码测试 end");
    }

    @Test
    public void testHack() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        hackers.put("java/io/File", "com/zhg2yqq/wheels/dynamic/code/hack/HackFile");
        CalTimeDTO calTime = new CalTimeDTO();
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);

        System.out.println("hack测试 start");
        Parameters pars0 = new Parameters();
        try {
            handler.runMethod(
                "package com.zhg2yqq.wheels.dynamic.code;\n" 
                + "import java.io.File;\n"
                + "public class FileTest {\n"
                + "    public void createFile() throws Exception {\n"
                + "        File file = new File(\"/zhg2yqq-test.txt\");\n"
                + "        file.createNewFile();\n"
                + "    }\n" 
                + "}",
                "createFile", pars0);
            Assert.assertFalse(true);
        } catch (ExecuteException e) {
            Throwable cause = e.getSourceCause();
            Assert.assertTrue(cause instanceof SecurityException);
        }
        System.out.println("hack测试 end");
    }

    @Test
    public void testCompileException() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, new CalTimeDTO(), hackers);

        System.out.println("编译异常测试 start");
        Parameters pars0 = new Parameters();
        try {
            handler.runMethod(
                "package com.zhg2yqq.wheels.dynamic.code;\n" 
                + "import java.io.File;\n"
                + "public class FileTest {\n"
                + "    public void createFile() throws Exception {\n"
                + "        File file = new + File(\"/zhg2yqq-test.txt\");\n"
                + "        file.createNewFile();\n"
                + "    }\n" 
                + "}",
                "createFile", pars0);
            Assert.assertFalse(true);
        } catch (CompileException e) {
            Assert.assertTrue(true);
            System.out.println(e.getCompileMessage());
        }
        System.out.println("编译异常测试 end");
    }

    @Test
    public void testExecuteException() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        CalTimeDTO calTime = new CalTimeDTO();
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter<ExecuteResult> executer = new ClassExecuter();
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);

        System.out.println("源码运行异常测试 start");
        Parameters pars0 = new Parameters();
        try {
            handler.runMethod(
                "package com.zhg2yqq.wheels.dynamic.code;\n" 
                + "public class ExecuteTest {\n"
                + "    public void calc() {\n"
                + "        int a = 9;\n"
                + "        int b = 0;\n"
                + "        int c = a / b;\n"
                + "    }\n" 
                + "}",
                "calc", pars0);
            Assert.assertFalse(true);
        } catch (ExecuteException e) {
            Throwable cause = e.getSourceCause();
            Assert.assertTrue(cause instanceof ArithmeticException);
        }
        System.out.println("源码运行测试 end");
    }

//    @Test
//    public void testStressTest() throws BaseDynamicException {
//        Map<String, String> hackers = new HashMap<>();
//        CalTimeDTO calTime = new CalTimeDTO();
//        IStringCompiler compiler = new StringJavaCompiler();
//        IClassExecuter<ExecuteResult> executer = new ClassExecuter();
//        RunSourceHandler handler = new RunSourceHandler(compiler, executer, calTime, hackers);
//
//        System.out.println("压力测试 start");
//        int stressTimes = 200;
//        
//        Parameters pars = new Parameters();
//        pars.add("    测试     ");
//        long current = System.currentTimeMillis();
//        for (int i = 0; i < stressTimes; i++) {
//            ExecuteResult result = handler.runMethod("package com.zhg2yqq.wheels.dynamic.code;\n"
//                    + "public class CodeTemplate" + i + " {\n"
//                    + "    public String trimStr(String str) {\n"
//                    + "        return str.trim() + \"" + i + "\";\n" 
//                    + "    }\n" 
//                    + "}", "trimStr",
//                    pars);
//            Assert.assertEquals("测试" + i, result.getReturnVal());
//        }
//        System.out.println("0 cost time: " + (System.currentTimeMillis() - current));
//        
//        current = System.currentTimeMillis();
//        for (int i = 0; i < stressTimes; i++) {
//            ExecuteResult result = handler.runMethod("package com.zhg2yqq.wheels.dynamic.code;\n"
//                    + "private class CodeTemplate {\n"
//                    + "    public String trimStr(String str) {\n"
//                    + "        return str.trim();\n" 
//                    + "    }\n" 
//                    + "}", "trimStr",
//                    pars);
//            Assert.assertEquals("测试", result.getReturnVal());
//        }
//        System.out.println("1 cost time: " + (System.currentTimeMillis() - current));
//        System.out.println("压力测试 end");
//    }
}