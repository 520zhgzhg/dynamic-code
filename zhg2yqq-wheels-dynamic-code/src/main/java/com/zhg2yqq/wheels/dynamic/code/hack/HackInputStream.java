/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.hack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月12日
 */
public class HackInputStream extends InputStream {
    public static final ThreadLocal<InputStream> PRIVATE_INPUT_STREAM = new ThreadLocal<>();

    @Override
    public int read() throws IOException {
        return 0;
    }

    public void set(String systemIn) {
        PRIVATE_INPUT_STREAM.set(new ByteArrayInputStream(systemIn.getBytes()));
    }

    public InputStream get() {
        return PRIVATE_INPUT_STREAM.get();
    }

    @Override
    public void close() {
        PRIVATE_INPUT_STREAM.remove();
    }
}
