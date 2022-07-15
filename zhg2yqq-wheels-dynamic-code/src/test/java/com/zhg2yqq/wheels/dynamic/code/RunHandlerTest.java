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
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter executer = new ClassExecuter();

        System.out.println("预编译测试 start");
        RunClassHandler handler = new RunClassHandler(compiler, executer, new CalTimeDTO(), hackers);
        // 待预编译源码
        List<String> preloadSources = new ArrayList<>();
        preloadSources.add("/*\r\n" 
                + " * Copyright (c) zhg2yqq Corp.\r\n"
                + " * All Rights Reserved.\r\n" 
                + " */\r\n" 
                + "package com.zhg2yqq.bill;\r\n"
                + "/**\r\n" 
                + " * @version zhg2yqq v1.0\r\n" 
                + " * @author 周海刚, 2022年6月8日\r\n"
                + " */\r\n" 
                + "public class Test {\r\n" 
                + "    /**\r\n" 
                + "     * 无聊时的精\r\n"
                + "     * @param args\r\n" 
                + "     */\r\n"
                + "    public static void main(String[] args) {\r\n" 
                + "        // 测试打印\r\n"
                + "        System.out.println(\"normal\");\r\n"
                + "        System.err.println(\"error\");\r\n" 
                + "    }\r\n" 
                + "}");
        preloadSources.add("package com.zhg2yqq.wheels.dynamic.code;\r\n"
                + "public class CodeTemplate {\r\n"
                + "    public String trimStr(String str) {\r\n"
                + "        return str.trim();\r\n" 
                + "    }\r\n" 
                + "}");
        // 预编译
        handler.preloadClass(preloadSources);

        // 执行类
        Parameters args0 = new Parameters();
        String[] pars0 = new String[0];
        args0.add(pars0);
        ExecuteResult result0 = handler.runClassJava("com.zhg2yqq.bill.Test", "main", args0);
        Assert.assertNull(result0.getReturnVal());

        Parameters pars1 = new Parameters();
        pars1.add("    测试1     ");
        ExecuteResult result1 = handler.runClassJava("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "trimStr",
                pars1);
        Assert.assertEquals("测试1", result1.getReturnVal());

        // 重新编译加载源码类
        Parameters pars2 = new Parameters();
        pars2.add("  zhg2yqq  测试2");
        ExecuteResult result2 = handler.runClassJava("com.zhg2yqq.wheels.dynamic.code.CodeTemplate", "trimStr",
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
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter executer = new ClassExecuter();

        System.out.println("源码测试 start");
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, new CalTimeDTO(), hackers);

        // 执行源码
        Parameters args0 = new Parameters();
        String[] pars0 = new String[0];
        args0.add(pars0);
        ExecuteResult result0 = handler.runSourceJava("/*\r\n" 
                + " * Copyright (c) zhg2yqq Corp.\r\n" 
                + " * All Rights Reserved.\r\n"
                + " */\r\n" 
                + "package com.zhg2yqq.bill;\r\n" 
                + "/**\r\n"
                + " * @version zhg2yqq v1.0\r\n" 
                + " * @author 周海刚, 2022年6月8日\r\n"
                + " */\r\n" 
                + "public class Test {\r\n" 
                + "    /**\r\n" 
                + "     * 无聊时的精\r\n"
                + "     * @param args\r\n" 
                + "     */\r\n"
                + "    public static void main(String[] args) {\r\n" 
                + "        // 测试打印\r\n"
                + "        System.out.println(\"normal\");\r\n"
                + "        System.err.println(\"error\");\r\n" 
                + "    }\r\n" 
                + "}",
                "main", args0);
        Assert.assertNull(result0.getReturnVal());

        Parameters pars1 = new Parameters();
        pars1.add("    测试1     ");
        ExecuteResult result1 = handler.runSourceJava("package com.zhg2yqq.wheels.dynamic.code;\r\n"
                + "public class CodeTemplate {\r\n"
                + "    public String trimStr(String str) {\r\n"
                + "        return str.trim();\r\n" 
                + "    }\r\n" 
                + "}", "trimStr",
                pars1);
        Assert.assertEquals("测试1", result1.getReturnVal());

        // 重新编译加载源码类
        Parameters pars2 = new Parameters();
        pars2.add("    测试2     ");
        ExecuteResult result2 = handler.runSourceJava(
                "package com.zhg2yqq.wheels.dynamic.code;\r\n" 
                + "public class CodeTemplate {\r\n"
                + "    public String trimStr(String str) {\r\n"
                + "        return str + \"zhg2yqq\";\r\n" 
                + "    }\r\n" 
                + "}",
                "trimStr", pars2, true);
        Assert.assertEquals("    测试2     zhg2yqq", result2.getReturnVal());

        result1 = handler.runSourceJava("package com.zhg2yqq.wheels.dynamic.code;\r\n"
                + "public class CodeTemplate {\r\n"
                + "    public String trimStr(String str) {\r\n"
                + "        return str.trim();\r\n" 
                + "    }\r\n" 
                + "}", "trimStr", pars1);
        Assert.assertEquals("    测试1     zhg2yqq", result1.getReturnVal());

        System.out.println("源码测试 end");
    }

    @Test
    public void testHack() throws BaseDynamicException {
        Map<String, String> hackers = new HashMap<>();
        hackers.put("java/io/File", "com/zhg2yqq/wheels/dynamic/code/hack/HackFile");
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter executer = new ClassExecuter();

        System.out.println("hack测试 start");
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, new CalTimeDTO(), hackers);
        Parameters pars0 = new Parameters();
        try {
            handler.runSourceJava(
                "package com.zhg2yqq.wheels.dynamic.code;\r\n" 
                + "import java.io.File;\r\n"
                + "public class FileTest {\r\n"
                + "    public void createFile() throws Exception {\r\n"
                + "        File file = new File(\"/zhg2yqq-test.txt\");\r\n"
                + "        file.createNewFile();\r\n"
                + "    }\r\n" 
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
        IClassExecuter executer = new ClassExecuter();

        System.out.println("编译异常测试 start");
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, new CalTimeDTO(), hackers);
        Parameters pars0 = new Parameters();
        try {
            handler.runSourceJava(
                "package com.zhg2yqq.wheels.dynamic.code;\r\n" 
                + "import java.io.File;\r\n"
                + "public class FileTest {\r\n"
                + "    public void createFile() throws Exception {\r\n"
                + "        File file = new + File(\"/zhg2yqq-test.txt\");\r\n"
                + "        file.createNewFile();\r\n"
                + "    }\r\n" 
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
        IStringCompiler compiler = new StringJavaCompiler();
        IClassExecuter executer = new ClassExecuter();

        System.out.println("源码运行异常测试 start");
        RunSourceHandler handler = new RunSourceHandler(compiler, executer, new CalTimeDTO(), hackers);
        Parameters pars0 = new Parameters();
        try {
            handler.runSourceJava(
                "package com.zhg2yqq.wheels.dynamic.code;\r\n" 
                + "public class ExecuteTest {\r\n"
                + "    public void calc() {\r\n"
                + "        int a = 9;\r\n"
                + "        int b = 0;\r\n"
                + "        int c = a / b;\r\n"
                + "    }\r\n" 
                + "}",
                "calc", pars0);
            Assert.assertFalse(true);
        } catch (ExecuteException e) {
            Throwable cause = e.getSourceCause();
            Assert.assertTrue(cause instanceof ArithmeticException);
        }
        System.out.println("源码运行测试 end");
    }
}