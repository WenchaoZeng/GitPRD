package com.yit.gitprd;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Web服务器
 */
public class WebServer {

    public WebServer() throws Exception {

        // 监听端口
        HttpServer server = null;
        int port = 8000;
        while (port < 9000) {
            try {
                server = HttpServer.create(new InetSocketAddress(port), 0);
                Global.httpServer = server;
                Global.port = port;
                break;
            } catch (java.net.BindException ex) {
                port++;
            }
        }

        Log.info("local webServer port open : " + port);

        // 开启web服务器
        server.setExecutor(Executors.newCachedThreadPool());
        server.createContext("/", new RootHandler());
        server.start();
    }

    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {

            // 请求参数
            String path = t.getRequestURI().getPath();


            if (path.endsWith("getData")) {
                t.getResponseHeaders().add("content-type", "application/json");
                t.sendResponseHeaders(200, "request sueccess".length());
                OutputStream os = t.getResponseBody();
                os.write("request sueccess".getBytes());
                os.close();
            }


            // 推送
            if (path.endsWith("push.html")) {
                t.getResponseHeaders().add("content-type", fileHeaders.get(".html"));
                t.sendResponseHeaders(200, 0);
                //Push.doPush(t.getResponseBody());
                Log.info("client connection closed. Remote address: %s", t.getRemoteAddress().toString());
                return;
            }

            String filePath = "web" + path;
            // 处理静态文件
            if (Files.exists(Paths.get(filePath))) {
                sendFile(t, filePath);
                return;
            }

        }

        static Map<String, String> fileHeaders = new HashMap<>();
        static {
            fileHeaders.put(".html", "text/html; charset=utf-8");
            fileHeaders.put(".css", "text/css; charset=utf-8");
            fileHeaders.put(".js", "application/x-javascript; charset=utf-8");
            fileHeaders.put(".jpg", "image/jpeg; charset=utf-8");
            fileHeaders.put(".gif", "image/gif; charset=utf-8");
        }

        public static void sendFile(HttpExchange t, String filePath) throws IOException {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));

            // 文件类型
            filePath = filePath.toLowerCase();
            for (Map.Entry<String, String> item : fileHeaders.entrySet()) {
                if (filePath.endsWith(item.getKey())) {
                    t.getResponseHeaders().add("content-type", item.getValue());
                }
            }

            send(t, bytes);
        }

        public static void send(HttpExchange t, byte[] bytes) throws IOException {
            send(t, 200, bytes);
        }

        public static void send(HttpExchange t, int code, byte[] bytes) throws IOException {
            t.sendResponseHeaders(code, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
