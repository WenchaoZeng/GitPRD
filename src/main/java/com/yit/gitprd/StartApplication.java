package com.yit.gitprd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: clive
 * @date: 2018/06/05
 * @since: 1.0
 */
@SpringBootApplication
@ComponentScan("com.yit.gitprd")
public class StartApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StartApplication.class);
        builder.headless(false).run(args);
        new AppUI();



    }
}
