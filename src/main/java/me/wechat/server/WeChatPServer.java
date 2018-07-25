package me.wechat.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.SocketAddress;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;


public class WeChatPServer {

    public static void main(String[] args) {
        test();
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(res -> {
            SocketAddress remoteAddress = res.connection().remoteAddress();
            System.out.println("remote address " + remoteAddress.toString());
            res.bodyHandler(body -> {
                String path = res.path();
                //区块链大数据分析页面
                if (path.equals("/index")) {
                    res.response().setChunked(true).write("<html><head></head><body>hello world</body></html>").end();
                } else {
                    if (body.length() > 0) {
                        res.response().end("<xml> <ToUserName>< ![CDATA[toUser] ]></ToUserName> <FromUserName>< ![CDATA[fromUser] ]></FromUserName> <CreateTime>12345678</CreateTime> <MsgType>< ![CDATA[text] ]></MsgType> <Content>< ![CDATA[你好] ]></Content> </xml>");
                        System.out.println("Request body: " + body.toString());
                    }
                }
            });
        });
        server.listen(443, res -> {
            if (res.succeeded()) {
                System.out.println("Server listening");
            } else {
                System.out.println(res.cause().getMessage());
            }
        });
    }

    public static void test() {
        SAXBuilder builder = new SAXBuilder();
        try {
            String xml = "<xml> <ToUserName>< ![CDATA[toUser] ]></ToUserName> <FromUserName>< ![CDATA[fromUser] ]></FromUserName> <CreateTime>12345678</CreateTime> <MsgType>< ![CDATA[text] ]></MsgType> <Content>< ![CDATA[你好] ]></Content> </xml>";
//            xml = xml.replaceAll("<\\s!\\[CDATA\\[", "");
//            xml = xml.replaceAll("\\]\\s\\]>", "");
            Document document = builder.build(new ByteBufInputStream(Buffer.buffer(xml).getByteBuf()));
            Element root = document.getRootElement();
            Element data = root.getChild("ToUserName");
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
