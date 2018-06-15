package com.yit.gitprd.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;

import java.io.File;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public class FileUtil extends FileUtils {
    private FileUtil() {}

    /**
     * 创建文件夹
     * 同时会创建父文件夹
     * @param path
     */
    public static void createFolders(String path) {
        if (StringUtil.isEmpty(path)) return;
        File file = new File(path);
        if (file.exists()) return;
        Assert.isTrue(file.mkdirs(), "文件夹创建失败");
    }

    /**
     * 路径是否存在
     *
     * @param path
     * @return
     */
    public static boolean exist(String path) {
        return new File(path).exists();
    }
}
