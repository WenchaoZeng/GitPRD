package com.yit.gitprd;

import com.yit.gitprd.cons.AppCons;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.ConnectorStartFailedException;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: clive
 * @date: 2018/06/05
 * @since: 1.0
 */
@SpringBootApplication
@ComponentScan("com.yit.gitprd")
public class StartApplication implements EmbeddedServletContainerCustomizer {

    public static void main(String[] args) {
        int port = AppCons.MIN_PORT;
        while (port < AppCons.MAX_PORT) {
            try {
                Global.port = port;
                startWebServer(args);
                break;
            } catch (ConnectorStartFailedException e) {
                port++;
            }
        }

        startAppUI();

    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(Global.port);
    }

    private static void startWebServer(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StartApplication.class);
        builder.headless(false).run(args);
    }

    private static void startAppUI() {
        new AppUI();
    }
}
