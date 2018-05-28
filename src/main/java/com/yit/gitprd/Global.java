package com.yit.gitprd;

/**
 * 全局存储区
 */
public class Global {

    /**
     * 软件版本号
     */
    public static String version = "0.3.0";

    /**
     * 网页端主入口
     */
    public static String url;

    /**
     *  WebServer 端口号
     */
    public static int port;

    /**
     * 日志
     */
    public static StringBuilder logs = new StringBuilder();

    public static boolean enablePushLog = false;
    public static boolean enableFrameLog = false;
}
