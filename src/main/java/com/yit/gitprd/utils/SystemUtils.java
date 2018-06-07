package com.yit.gitprd.utils;

import java.io.File;

/**
 * @author: clive
 * @date: 2018/06/07
 * @since: 1.0
 */
public class SystemUtils {

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static void main(String[] args) {
        String userHome = getUserHome();
        File prds = new File(userHome + "/prds");
        if (!prds.exists()) {
            prds.mkdirs();
        }
        System.out.println(userHome);
    }
}
