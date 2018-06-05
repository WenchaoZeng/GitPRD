package com.yit.gitprd;

import java.awt.*;
import javax.swing.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *  主视图
 */
public class AppUI {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public AppUI() {
        // 创建Frame
        JFrame frame = new JFrame("GitPrd");
        final JFXPanel webBrowser = new JFXPanel();
        frame.setLayout(new BorderLayout());
        frame.add(webBrowser, BorderLayout.CENTER);

        Platform.runLater(()->{
            // 创建 Group 场景
            Group root = new Group();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            webBrowser.setScene(scene);
            double widthDouble = Integer.valueOf(WIDTH).doubleValue();
            double heightDouble = Integer.valueOf(HEIGHT).doubleValue();

            // 创建Vbox
            VBox box = new VBox(10);

            // 创建WebView
            WebView view = new WebView();
            //todo 自适应窗口的大小
            view.setMinSize(widthDouble, heightDouble);
            view.setPrefSize(widthDouble, heightDouble);
            final WebEngine eng = view.getEngine();

            eng.load("http://127.0.0.1:8000/html/index.html");
            // bind
            root.getChildren().add(view);
            box.getChildren().add(view);
            root.getChildren().add(box);
        });

        // set configuration
        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation((screenWidth - WIDTH) / 2, (screenHeight - HEIGHT) / 2);

    }
}
