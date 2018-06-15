package com.yit.gitprd.websocket;

import com.alibaba.fastjson.JSON;
import com.yit.gitprd.pojo.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint("/notice_socket")
@Component
public class NoticeSocket {

    private static final Logger logger = LoggerFactory.getLogger(NoticeSocket.class);

    /**
     * 有效的会话
     */
    private static List<Session> sessions = new ArrayList<>();


    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("客户端发送消息, msg={}, sid={}", message, session.getId());
    }

    /**
     * 发送通知
     * @param notice
     */
    public void sendNotice(Notice notice) {
        sendNotice(JSON.toJSON(notice).toString());
    }
    /**
     * 发送通知
     *
     * @param content
     */
    public void sendNotice(String content) {
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendText(content);
            } catch (IOException e) {
                logger.error("sendNotice", e);
            }
        });
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("客户端建立链接, sid={}", session.getId());
        try {
            sessions.add(session);
        } catch (Exception e) {
            logger.error("onOpen Exception", e);
        }

    }

    @OnClose
    public synchronized void onClose(Session session, CloseReason reason) {
        try {
            logger.info("客户端关闭, sid={}, reason={}", session.getId(), reason.toString());
            sessions.remove(session);
        } catch (Exception e) {
            logger.error("onClose Exception", e);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("服务器出错 重新连接中..., sid={}", session.getId());
        //logger.error("onError ", throwable);
    }
}
