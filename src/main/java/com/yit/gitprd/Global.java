package com.yit.gitprd;

import org.springframework.boot.builder.SpringApplicationBuilder;
/**
 * 全局存储区
 */
public class Global {

    /**
     *  WebServer 端口号
     */
    public static int port;

    /**
     *  HTTP Server
     */
    public static SpringApplicationBuilder httpBuilder;

    /**
     * 日志
     */
    public static StringBuilder logs = new StringBuilder();

    public static boolean enablePushLog = false;
    public static boolean enableFrameLog = false;

}
