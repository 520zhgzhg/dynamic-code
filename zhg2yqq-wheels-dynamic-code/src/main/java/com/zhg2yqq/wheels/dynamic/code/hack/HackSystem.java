/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.hack;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.channels.Channel;
import java.util.Map;
import java.util.Properties;

/**
 * 将标准输出对象替换为指定的PrintStream对象
 * 因为标准输出是虚拟机全局共享的资源，不能让客户端与服务器争夺System资源，因此需要将System替换为我们自己写的HackSystem
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月12日
 */
public final class HackSystem {

    private HackSystem() {
    }

    public final static InputStream in = new HackInputStream();
    public final static PrintStream out = new HackPrintStream();
    public final static PrintStream err = out;

    /**
     * 获取当前线程输出流中的内容
     */
    public static String getBufferString() {
        return ((HackPrintStream) out).toString();
    }

    /**
     * 关闭当前线程的输入输出流
     */
    public static void closeBuffer() {
        ((HackInputStream) in).close();
        out.close();
    }

    private static volatile SecurityManager securityManager = null;

    public static String clearProperty(String key) {
        throw new SecurityException("Use hazardous method: System.clearProperty().");
    }

    public static Console console() {
        throw new SecurityException("Use hazardous method: System.console().");
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void exit(int status) {
        throw new SecurityException("Use hazardous method: System.exit().");
    }

    public static void gc() {
        throw new SecurityException("Use hazardous method: System.gc().");
    }

    public static Map<String, String> getenv() {
        throw new SecurityException("Use hazardous method: System.getenv().");
    }

    public static String getenv(String name) {
        throw new SecurityException("Use hazardous method: System.getenv().");
    }

    public static Properties getProperties() {
        throw new SecurityException("Use hazardous method: System.getProperties().");
    }

    public static String getProperty(String key) {
        throw new SecurityException("Use hazardous method: System.getProperty().");
    }

    public static String getProperty(String key, String def) {
        throw new SecurityException("Use hazardous method: System.getProperty().");
    }

    public static SecurityManager getSecurityManager() {
        throw new SecurityException("Use hazardous method: System.getSecurityManager().");
    }

    public static int identityHashCode(Object x) {
        return System.identityHashCode(x);
    }

    public static Channel inheritedChannel() throws IOException {
        throw new SecurityException("Use hazardous method: System.inheritedChannel().");
    }

    public static String lineSeparator() {
        return System.lineSeparator();
    }

    public static void load(String filename) {
        throw new SecurityException("Use hazardous method: System.load().");
    }

    public static void loadLibrary(String libname) {
        throw new SecurityException("Use hazardous method: System.loadLibrary.");
    }

    public static String mapLibraryName(String libname) {
        throw new SecurityException("Use hazardous method: System.mapLibraryName().");
    }

    public static long nanoTime() {
        return System.nanoTime();
    }

    public static void runFinalization() {
        throw new SecurityException("Use hazardous method: System.runFinalization().");
    }

    @Deprecated
    public static void runFinalizersOnExit(boolean value) {
        throw new SecurityException("Use hazardous method: System.runFinalizersOnExit().");
    }

    public static void setErr(PrintStream err) {
        throw new SecurityException("Use hazardous method: System.setErr().");
    }

    public static void setIn(InputStream in) {
        throw new SecurityException("Use hazardous method: System.setIn().");
    }

    public static void setOut(PrintStream out) {
        throw new SecurityException("Use hazardous method: System.setOut().");
    }

    public static void setProperties(Properties props) {
        throw new SecurityException("Use hazardous method: System.setProperties().");
    }

    public static String setProperty(String key, String value) {
        throw new SecurityException("Use hazardous method: System.setProperty().");
    }

    public static void setSecurityManager(final SecurityManager s) {
        throw new SecurityException("Use hazardous method: System.setSecurityManager().");
    }
}
