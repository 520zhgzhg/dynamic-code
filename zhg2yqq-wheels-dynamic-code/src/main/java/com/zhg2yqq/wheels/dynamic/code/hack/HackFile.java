/*
 * Copyright (c) zhg2yqq Corp.
 * All Rights Reserved.
 */
package com.zhg2yqq.wheels.dynamic.code.hack;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

/**
 * 禁止文件操作
 * @version zhg2yqq v1.0
 * @author 周海刚, 2022年7月12日
 */
public class HackFile extends File {
    private static final long serialVersionUID = 1L;

    public HackFile(URI uri) {
        super(uri);
    }

    public HackFile(String parent, String child) {
        super(parent, child);
    }

    public HackFile(String pathname) {
        super(pathname);
    }

    public HackFile(File arg0, String arg1) {
        super(arg0, arg1);
    }

    @Override
    public boolean canExecute() {
        throw new SecurityException("Use hazardous method: File.canExecute().");
    }

    @Override
    public boolean canRead() {
        throw new SecurityException("Use hazardous method: File.canRead().");
    }

    @Override
    public boolean canWrite() {
        throw new SecurityException("Use hazardous method: File.canWrite().");
    }

    @Override
    public boolean createNewFile() throws IOException {
        throw new SecurityException("Use hazardous method: File.createNewFile().");
    }

    @Override
    public boolean delete() {
        throw new SecurityException("Use hazardous method: File.delete().");
    }

    @Override
    public void deleteOnExit() {
        throw new SecurityException("Use hazardous method: File.deleteOnExit().");
    }

    @Override
    public boolean equals(Object obj) {
        throw new SecurityException("Use hazardous method: File.equals(obj).");
    }

    @Override
    public boolean exists() {
        throw new SecurityException("Use hazardous method: File.exists().");
    }

    @Override
    public File getAbsoluteFile() {
        throw new SecurityException("Use hazardous method: File.getAbsoluteFile().");
    }

    @Override
    public String getAbsolutePath() {
        throw new SecurityException("Use hazardous method: File.getAbsolutePath().");
    }

    @Override
    public File getCanonicalFile() throws IOException {
        throw new SecurityException("Use hazardous method: File.getCanonicalFile().");
    }

    @Override
    public String getCanonicalPath() throws IOException {
        throw new SecurityException("Use hazardous method: File.getCanonicalPath().");
    }

    @Override
    public long getFreeSpace() {
        throw new SecurityException("Use hazardous method: File.getFreeSpace().");
    }

    @Override
    public String getName() {
        throw new SecurityException("Use hazardous method: File.getName().");
    }

    @Override
    public String getParent() {
        throw new SecurityException("Use hazardous method: File.getParent().");
    }

    @Override
    public File getParentFile() {
        throw new SecurityException("Use hazardous method: File.getParentFile().");
    }

    @Override
    public String getPath() {
        throw new SecurityException("Use hazardous method: File.getPath().");
    }

    @Override
    public long getTotalSpace() {
        throw new SecurityException("Use hazardous method: File.getTotalSpace().");
    }

    @Override
    public long getUsableSpace() {
        throw new SecurityException("Use hazardous method: File.getUsableSpace().");
    }

    @Override
    public boolean isAbsolute() {
        throw new SecurityException("Use hazardous method: File.isAbsolute().");
    }

    @Override
    public boolean isDirectory() {
        throw new SecurityException("Use hazardous method: File.isDirectory().");
    }

    @Override
    public boolean isFile() {
        throw new SecurityException("Use hazardous method: File.isFile().");
    }

    @Override
    public boolean isHidden() {
        throw new SecurityException("Use hazardous method: File.isHidden().");
    }

    @Override
    public long lastModified() {
        throw new SecurityException("Use hazardous method: File.lastModified().");
    }

    @Override
    public long length() {
        throw new SecurityException("Use hazardous method: File.length().");
    }

    @Override
    public String[] list() {
        throw new SecurityException("Use hazardous method: File.list().");
    }

    @Override
    public String[] list(FilenameFilter filter) {
        throw new SecurityException("Use hazardous method: File.list(FilenameFilter filter).");
    }

    @Override
    public File[] listFiles() {
        throw new SecurityException("Use hazardous method: File.listFiles().");
    }

    @Override
    public File[] listFiles(FileFilter filter) {
        throw new SecurityException("Use hazardous method: File.listFiles(FileFilter filter).");
    }

    @Override
    public File[] listFiles(FilenameFilter filter) {
        throw new SecurityException("Use hazardous method: File.listFiles(FilenameFilter filter).");
    }

    @Override
    public boolean mkdir() {
        throw new SecurityException("Use hazardous method: File.mkdir().");
    }

    @Override
    public boolean mkdirs() {
        throw new SecurityException("Use hazardous method: File.mkdirs().");
    }

    @Override
    public boolean renameTo(File dest) {
        throw new SecurityException("Use hazardous method: File.renameTo(File dest).");
    }

    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        throw new SecurityException("Use hazardous method: File.setExecutable(boolean executable, boolean ownerOnly).");
    }

    @Override
    public boolean setExecutable(boolean executable) {
        throw new SecurityException("Use hazardous method: File.setExecutable(boolean executable).");
    }

    @Override
    public boolean setLastModified(long time) {
        throw new SecurityException("Use hazardous method: File.setLastModified(long time).");
    }

    @Override
    public boolean setReadOnly() {
        throw new SecurityException("Use hazardous method: File.setReadOnly().");
    }

    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {
        throw new SecurityException("Use hazardous method: File.setReadable(boolean readable, boolean ownerOnly).");
    }

    @Override
    public boolean setReadable(boolean readable) {
        throw new SecurityException("Use hazardous method: File.setReadable(boolean readable).");
    }

    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {
        throw new SecurityException("Use hazardous method: File.setWritable(boolean readable, boolean ownerOnly).");
    }

    @Override
    public boolean setWritable(boolean writable) {
        throw new SecurityException("Use hazardous method: File.setWritable(boolean readable).");
    }

    @Override
    public Path toPath() {
        throw new SecurityException("Use hazardous method: File.toPath().");
    }

    @Override
    public String toString() {
        throw new SecurityException("Use hazardous method: File.toString().");
    }

    @Override
    public URI toURI() {
        throw new SecurityException("Use hazardous method: File.toURI().");
    }

    @Override
    public URL toURL() throws MalformedURLException {
        throw new SecurityException("Use hazardous method: File.toURL().");
    }

}
