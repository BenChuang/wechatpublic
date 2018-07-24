package me.wechat.server;


import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

import java.time.LocalDateTime;


public class WeChatPServer {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(res -> {
            System.out.println("request in");
            String echoStr = res.getParam("echoStr");
            System.out.println(echoStr);
            res.response().end(echoStr);
        });
        server.listen(443, res -> {
            if (res.succeeded()) {
                System.out.println("Server listening");
            } else {
                System.out.println(res.cause().getMessage());
            }
        });
    }
}
