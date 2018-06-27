package com.yit.gitprd;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import com.yit.gitprd.cons.AppCons;
import com.yit.gitprd.cons.GitPrdCons;
import com.yit.gitprd.pojo.GitStatus;
import com.yit.gitprd.pojo.Notice;
import com.yit.gitprd.service.GitPrdService;
import com.yit.gitprd.utils.SpringUtils;
import com.yit.gitprd.websocket.NoticeSocket;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主视图
 */
public class AppUI {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUI.class);

    private GitPrdService gitPrdService;
    private NoticeSocket noticeSocket ;

    // 场景
    private Scene scene;
    // webView
    private WebView view;

    public AppUI() {
        // init bean
        gitPrdService = SpringUtils.getBean(GitPrdService.class);
        noticeSocket = SpringUtils.getBean(NoticeSocket.class);

        // 创建Frame
        JFrame frame = new JFrame("PRD管理系统");
        final JFXPanel webBrowser = new JFXPanel();
        frame.setLayout(new BorderLayout());
        frame.add(webBrowser, BorderLayout.CENTER);

        Platform.runLater(() -> {
            // 创建 Group 场景
            Group root = new Group();
            scene = new Scene(root, WIDTH, HEIGHT);
            webBrowser.setScene(scene);
            double widthDouble = Integer.valueOf(WIDTH).doubleValue();
            double heightDouble = Integer.valueOf(HEIGHT).doubleValue();
            // 创建Vbox
            VBox box = new VBox(10);

            view = new WebView();
            view.autosize();
            view.setMinSize(widthDouble, heightDouble);
            view.setPrefSize(widthDouble, heightDouble);
            final WebEngine eng = view.getEngine();

            StringBuilder url = new StringBuilder(AppCons.PROTOCOL);
            url.append(AppCons.LOCALHOST)
                    .append(":")
                    .append(Global.port)
                    .append(AppCons.DEFAULT_URL);
            eng.load(url.toString());
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

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension dimension = e.getComponent().getSize();
                view.setMinSize(Integer.valueOf(dimension.width).doubleValue(), Integer.valueOf(dimension.height).doubleValue());
                view.setPrefSize(Integer.valueOf(dimension.width).doubleValue(), Integer.valueOf(dimension.height).doubleValue());
            }
        });

        // 窗口被激活立即推送
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                new Thread(() -> asyncRun()).start();
            }
        });
    }

    private void asyncRun() {
        LOGGER.info("window focusGained noticeTask . . .");
        Notice notice;
        try {
            java.util.List<GitStatus> allBranchStatus = gitPrdService.getAllBranchStatus();
            notice = Notice.newBranchStatusNotice(allBranchStatus);
        } catch (IllegalArgumentException e) {
            notice = Notice.newMsgNotice(e.getMessage());
        } catch (GitAPIException e) {
            LOGGER.error("getAllBranchStatus", e);
            notice = Notice.newMsgNotice(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            LOGGER.error("getAllBranchStatus", e);
            notice = Notice.newMsgNotice(GitPrdCons.SYS_ERROR);
        }
        noticeSocket.sendNotice(notice);
    }
}
